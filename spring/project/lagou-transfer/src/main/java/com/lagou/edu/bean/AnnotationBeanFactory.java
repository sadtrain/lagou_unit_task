package com.lagou.edu.bean;

import com.lagou.edu.anno.Autowired;
import com.lagou.edu.anno.Component;
import com.lagou.edu.anno.Service;
import com.lagou.edu.anno.Transactional;
import com.lagou.edu.proxy.TransactionCgLibProxy;
import com.lagou.edu.proxy.TransactionJDKProxy;
import com.mysql.jdbc.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @Description : bean工厂
 * @Author : ZGS
 * @Date: 2020-11-01 17:37
 */
public class AnnotationBeanFactory<T> {

    //beanMap存放beanName对bean的映射
    public static HashMap<String,Object> beanMap = new HashMap<>();
    //每个beanName对应的beanType
    public static HashMap<String,String> nameTypeMap = new HashMap<>();
    //bean实例化之前的一些参数暂时存到这里
    public static List<BeanDefinition> beanDefinitions = new ArrayList<>();
    public static String componentScanPath = "com.lagou.edu";
    public static void init(){
        try {
            //在使用注解扫描时，我遇到的第一个问题是，如何扫描一个包下面的所有类
            //为此，我翻阅了spring源码，发现org.springframework.core.io.support.PathMatchingResourcePatternResolver#getResources下有扫描类的方法
            //阅读后发现，其实是根据包路径获取到真正的文件路径，然后获取包下所有的class文件
            //对于题目，我应该不需要获取全部的文件，而是扫描后转换为类路径，然后使用反射获取对象即可

            //首先把这些信息都读进来，包括注解信息，读取到一个beandefinition中。等类都初始化之后，再读取每个beandefinition，如果有autowired，去给每个bean注入属性
            //获取所有的类路径
            List<String> allClassPaths = getClassPaths(componentScanPath);
            //beanDefinition中有bean的class实例，还有bean的Name，以及他们需要注入的属性
            beanDefinitions = getAllBeanDefinitions(allClassPaths);

            //bean生命周期开始
            //实例化每个bean
            for (BeanDefinition beanDefinition : beanDefinitions) {
                beanMap.put(beanDefinition.getBeanName(),beanDefinition.getaClass().newInstance());
            }
            //bean的name对type的映射
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?>[] interfaces = beanDefinition.getaClass().getInterfaces();
                if(interfaces.length == 0){
                    nameTypeMap.put(beanDefinition.getBeanName(),beanDefinition.getaClass().getName());
                }else{
                    nameTypeMap.put(beanDefinition.getBeanName(),interfaces[0].getName());
                }
            }
            //这一步相当于beanPostProcessor,bean实例化后，对属性进行自动注入
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Object bean = beanMap.get(beanDefinition.getBeanName());
                for (String autowiredBean : beanDefinition.getAutowiredBeans()) {
                    Class<?> aClass1 = beanDefinition.getaClass();
//                    Class<?> aClass = bean.getClass();
                    Field declaredField = aClass1.getDeclaredField(autowiredBean);
                    if(declaredField!=null){
                    for (Map.Entry<String, String> entry : nameTypeMap.entrySet()) {
                        if(entry.getValue().equalsIgnoreCase(declaredField.getType().getName())){
                            declaredField.setAccessible(true);
                            declaredField.set(bean,beanMap.get(entry.getKey()));
                        }
                    }


                    }
                }
            }

            //接下来需要装配事务,把map中的实例转化为代理对象
            for (BeanDefinition beanDefinition : beanDefinitions) {
                boolean transactional = beanDefinition.isTransactional();
                if(transactional){
                    if(beanDefinition.getaClass().getInterfaces().length == 0){
                        Enhancer enhancer = new Enhancer();
                        enhancer.setCallback(new TransactionCgLibProxy(beanMap.get(beanDefinition.getBeanName())));
                        enhancer.setSuperclass(beanDefinition.getaClass());
                        Object cglibProxyInstance = enhancer.create();
                        beanMap.put(beanDefinition.getBeanName(),cglibProxyInstance);
                    }else{
                        TransactionJDKProxy transactionJDKProxy = new TransactionJDKProxy(beanMap.get(beanDefinition.getBeanName()));
                        Object proxyObject = transactionJDKProxy.getProxyObject(new Object());
                        beanMap.put(beanDefinition.getBeanName(),proxyObject);
                    }
                }
            }
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | UnsupportedEncodingException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static List<BeanDefinition> getAllBeanDefinitions(List<String> allClassPaths) throws ClassNotFoundException {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String classPath : allClassPaths) {
            Class<?> aClass = Class.forName(classPath.replace(".class",""));
            Transactional declaredAnnotation = aClass.getAnnotation(Transactional.class);
            Component declaredAnnotation1 = aClass.getDeclaredAnnotation(Component.class);
            Service declaredAnnotation2 = aClass.getDeclaredAnnotation(Service.class);
            if(declaredAnnotation1!=null||declaredAnnotation2!=null){
                BeanDefinition beanDefinition = new BeanDefinition();
                if(declaredAnnotation1!=null) {
                    setBeanName(aClass, beanDefinition,declaredAnnotation1.value());
                }
                if(declaredAnnotation2!=null) {
                    setBeanName(aClass, beanDefinition, declaredAnnotation2.value());
                }
                List<String> autowireds = new ArrayList<>();
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Autowired annotation = declaredField.getAnnotation(Autowired.class);
                    if(annotation!=null){
                        autowireds.add(declaredField.getName());
                    }
                }
                if(declaredAnnotation!=null){
                    beanDefinition.setTransactional(true);
                }
                beanDefinition.setaClass(aClass);
                beanDefinition.setAutowiredBeans(autowireds);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;

    }

    private static void setBeanName(Class<?> aClass, BeanDefinition beanDefinition, String value) {
            if (StringUtils.isNullOrEmpty(value)) {
                char c = aClass.getSimpleName().charAt(0);
                String temp = new String(new char[]{c});
                //这里要用simpleName获取不带package的类名
                beanDefinition.setBeanName(aClass.getSimpleName().replaceFirst(temp, temp.toLowerCase()));
            } else {
                beanDefinition.setBeanName(value);
            }
    }

    private static List<String> getClassPaths(String componentScanPath) throws UnsupportedEncodingException {
        List<String> classPaths = new ArrayList<>();
        String packageFilePath = componentScanPath.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        URL resource = AnnotationBeanFactory.class.getResource("/");

        String realPathWithFile = resource.toString();
        realPathWithFile = URLDecoder.decode(realPathWithFile, "utf-8");
        String s = realPathWithFile.replaceFirst("file:"+"/", "");
        s = s + "\\" + packageFilePath;
        File rootDir = new File(s);
        List<File> files = new ArrayList<>();
        //递归获取全部文件
        getAllFiles(rootDir,files);
        for (File file : files) {
            String packagePath = getPackagePath(file.getAbsolutePath(), packageFilePath);
            classPaths.add(packagePath);
        }
        return classPaths;
    }

    private static String getPackagePath(String absolutePath, String packageFilePath) {
        String substring = absolutePath.substring(absolutePath.indexOf(packageFilePath));
        return substring.replaceAll(Matcher.quoteReplacement(File.separator), "\\.");
    }

    public static void getAllFiles(File dir,List<File> files){
        if(dir.isFile()){
            files.add(dir);
            return;
        }else{
            for (File file : dir.listFiles()) {
                getAllFiles(file,files);
            }
        }
    }
    public static void main(String[] args) {
//        getClassPaths("com.lagou.edu");
//        getA("com.lagou.edu");
    }
//    static {
//        try {
//            InputStream in = AnnotationBeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
//            Document document = new SAXReader().read(in);
//            Element rootElement = document.getRootElement();
//            List<Element> bean = rootElement.elements("bean");
//            for (Element element : bean) {
//                String id = element.attributeValue("id");
//                String aClass = element.attributeValue("class");
//                Class<?> aClass1 = Class.forName(aClass);
//                beanMap.put(id,o);
//            }
//            List<Element> list = rootElement.selectNodes("//property");
//            for (Element element : list) {
//                String name = element.attributeValue("name");
//                String ref = element.attributeValue("ref");
//                String id = element.getParent().attributeValue("id");
//                Object parent = beanMap.get(id);
//                Object refBean = beanMap.get(ref);
//                Field[] declaredFields = parent.getClass().getDeclaredFields();
//                Method[] declaredMethods = parent.getClass().getDeclaredMethods();
//                for (Method declaredMethod : declaredMethods) {
//                    if(declaredMethod.getName().equalsIgnoreCase("set"+name)){
//                        declaredMethod.invoke(parent,refBean);
//                    }
//                }
//
//                beanMap.put(id,parent);
//            }
//        } catch (DocumentException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
    public static Object getBean(String beanId){
        return beanMap.get(beanId);
    }
//    public static Object getBeanByType(Class<?> clazz){
//
//    }
}
