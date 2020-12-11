//package com.lagou.edu.bean;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @Description : bean工厂
// * @Author : ZGS
// * @Date: 2020-11-01 17:37
// */
//public class BeanFactory <T> {
//
//    public static HashMap<String,Object> beanMap = new HashMap<>();
//
//    static {
//        try {
//            InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
//            Document document = new SAXReader().read(in);
//            Element rootElement = document.getRootElement();
//            List<Element> bean = rootElement.elements("bean");
//            for (Element element : bean) {
//                String id = element.attributeValue("id");
//                String aClass = element.attributeValue("class");
//                Class<?> aClass1 = Class.forName(aClass);
//                Object o = aClass1.newInstance();
////                List<Element> properties = element.elements("property");
////                if(properties!=null&&properties.size()>0){
////                    Field[] declaredFields = aClass1.getDeclaredFields();
////                    for (Element property : properties) {
////                        String name = property.attributeValue("name");
////                        String ref = property.attributeValue("ref");
////                        Object o1 = beanMap.get(ref);
////                        for (Field declaredField : declaredFields) {
////                            if(declaredField.getName().equals(name)){
////                                declaredField.set(name,o1);
////                            }
////                        }
////
////                    }
////                }
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
//    public static Object getBean(String beanId){
//        return beanMap.get(beanId);
//    }
//}
