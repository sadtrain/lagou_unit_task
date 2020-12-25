package com.lagou;

import java.io.*;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-12-23 21:27
 */
public class WebClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if(name.equals("java.lang.Object")){
                return Object.class;
            }
            if(c!=null){
                return c;
            }
            InputStream is = null;
            try {
                is = getClassInputStream(name);
            } catch (FileNotFoundException e) {
                return super.loadClass(name);
            }
            

            byte[] bt = new byte[0];
            try {
                bt = new byte[is.available()];
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                is.read(bt);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Class<?> aClass = defineClass("Person", bt, 0, bt.length);
//        Class<?> aClass1 = defineClass(classPath, bt, 0, bt.length);
            return aClass;
        }
    }

    private InputStream getClassInputStream(String name) throws FileNotFoundException {
        InputStream is = new FileInputStream(new File(name));
        return is;
    }


}
