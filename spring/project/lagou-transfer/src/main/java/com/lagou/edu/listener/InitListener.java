package com.lagou.edu.listener;

import com.lagou.edu.bean.AnnotationBeanFactory;
import com.lagou.edu.proxy.TransactionJDKProxy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-12-10 21:58
 */
@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        AnnotationBeanFactory.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
