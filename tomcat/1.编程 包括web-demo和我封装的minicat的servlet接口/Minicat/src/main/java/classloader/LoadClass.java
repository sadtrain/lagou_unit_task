package classloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadClass {
    private WebClassLoader webClassLoader;
    public static Map<String,String> allLoadedClassAndClassLoaders = new HashMap<>();
    public static Map<String,Class<?>> allLoadedClasses = new HashMap<>();
    public LoadClass(WebClassLoader webClassLoader){
        this.webClassLoader = webClassLoader;
    }
    public void loadWebs(String webAppPath) throws ClassNotFoundException {
        File file = new File(webAppPath);
        File[] files = file.listFiles();
        for (File file1 : files) {
            loadOneWeb(file1);
        }

    }
    public void loadWeb(String webPath) throws ClassNotFoundException {
        loadOneWeb(new File(webPath));
    }

    private void loadOneWeb(File file1) throws ClassNotFoundException {
        String classesPath = getClassesPath(file1);
        File file = new File(classesPath);
        List<File> classFiles = new ArrayList<>();
        getAllClasses(file,classFiles);
        for (File classFile : classFiles) {
            Class<?> aClass = webClassLoader.loadClass(classFile.getAbsolutePath());
            allLoadedClassAndClassLoaders.put(aClass.getName(),aClass.getClassLoader().toString());
            allLoadedClasses.put(aClass.getName(),aClass);
        }
    }

    private void getAllClasses(File file,List<File> files) {
        if(file.isDirectory()){
            for (File listFile : file.listFiles()) {
                getAllClasses(listFile,files);
            }
        }else{
            files.add(file);
        }

    }

    public void printAll(){
        for(Map.Entry<String,String> entry:allLoadedClassAndClassLoaders.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
    private String getClassesPath(File file1) {
        File[] files = file1.listFiles();
        for (File file : files) {
            if("WEB-INF".equalsIgnoreCase(file.getName())){
                for (File listFile : file.listFiles()) {
                    if("classes".equalsIgnoreCase(listFile.getName())){
                        return listFile.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }
}
