package com.lagou.edu.bean;

import java.util.List;

public class BeanDefinition {
    private String beanName;
    private boolean transactional = false;
    private List<String> autowiredBeans = null;
    private Class<?> aClass;
    private String classPath;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public boolean isTransactional() {
        return transactional;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    public List<String> getAutowiredBeans() {
        return autowiredBeans;
    }

    public void setAutowiredBeans(List<String> autowiredBeans) {
        this.autowiredBeans = autowiredBeans;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }
}
