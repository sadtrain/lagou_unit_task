package com.lagou.edu.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-11-08 12:21
 */
public class ConnectionUtils {


    private static Connection connection;
    private static ThreadLocal<Connection> threadLocalConn = new ThreadLocal<>();
    public static Connection getConnection(){
        if(connection==null){
            synchronized (ConnectionUtils.class){
                try {
                    connection = DruidUtils.getInstance().getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return connection;
    }
    public static Connection getCurrentThreadConn(){
        if(threadLocalConn.get()==null){
            synchronized (ConnectionUtils.class){
                try {
                    connection = DruidUtils.getInstance().getConnection();
                    threadLocalConn.set(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return threadLocalConn.get();
    }
    public static void clear(){
        threadLocalConn.remove();
    }
}
