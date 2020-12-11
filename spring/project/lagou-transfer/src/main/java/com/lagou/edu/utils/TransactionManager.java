package com.lagou.edu.utils;

import java.lang.reflect.Proxy;
import java.sql.SQLException;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-11-08 21:08
 */
public class TransactionManager {

    private static TransactionManager transactionManager = new TransactionManager();
    public static TransactionManager getInstance(){
        return transactionManager;
    }
    public void beginTransaction() throws SQLException {
        ConnectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }
    public void closeTransaction() throws SQLException {
        ConnectionUtils.getCurrentThreadConn().close();
        ConnectionUtils.clear();
    }
    public void commit() throws SQLException {
        ConnectionUtils.getCurrentThreadConn().commit();
        ConnectionUtils.clear();
    }
    public void rollBack() throws SQLException {
        ConnectionUtils.getCurrentThreadConn().rollback();
        ConnectionUtils.clear();
    }
}
