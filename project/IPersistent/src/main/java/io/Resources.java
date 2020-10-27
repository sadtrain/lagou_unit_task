package io;

import java.io.InputStream;

/**
 * @Description : 读取输入流
 * @Author : ZGS
 * @Date: 2020-08-15 15:24
 */
public class Resources {

    public static InputStream getResourceAsStream(String path){
        InputStream inputStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return inputStream;
    }
}
