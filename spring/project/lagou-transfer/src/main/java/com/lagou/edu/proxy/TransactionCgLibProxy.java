package com.lagou.edu.proxy;

import com.lagou.edu.utils.TransactionManager;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-12-10 21:08
 */
public class TransactionCgLibProxy implements MethodInterceptor {
    private Object proxyObject;

    public TransactionCgLibProxy(Object o){
        this.proxyObject = o;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object o1 = null;
        TransactionManager.getInstance().beginTransaction();
        try{
            o1 = methodProxy.invokeSuper(proxyObject, objects);
        }catch (Exception e){
            TransactionManager.getInstance().rollBack();
            TransactionManager.getInstance().closeTransaction();
        }finally {
            TransactionManager.getInstance().closeTransaction();
        }
        return o1;
    }
}
