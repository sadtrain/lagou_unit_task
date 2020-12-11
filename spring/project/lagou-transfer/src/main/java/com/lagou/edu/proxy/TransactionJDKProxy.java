package com.lagou.edu.proxy;

import com.lagou.edu.utils.ConnectionUtils;
import com.lagou.edu.utils.TransactionManager;
import org.junit.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-11-08 21:16
 */
public class TransactionJDKProxy {

    private Object proxyObject;

    public TransactionJDKProxy(Object o){
        this.proxyObject = o;
    }

    public Object getProxyObject(Object object){
        Object o = Proxy.newProxyInstance(proxyObject.getClass().getClassLoader(), proxyObject.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                TransactionManager transactionManager = TransactionManager.getInstance();
                try {

                    transactionManager.beginTransaction();
                    Object invoke = method.invoke(proxyObject, args);
                    transactionManager.commit();
                    transactionManager.closeTransaction();
                    return invoke;
                } catch (Exception e) {
                    transactionManager.rollBack();
                    transactionManager.closeTransaction();
                    throw e;
                }
            }
        });
        return o;
    }
}
