package com.zc.spring;

public class BeanDefinition {

    private Class beanClass;
    private String isLazy;
    private String scope;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getIsLazy() {
        return isLazy;
    }

    public void setIsLazy(String isLazy) {
        this.isLazy = isLazy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
