package com.zc.spring;

import com.zc.demo.AppConfig;

public class MySpringApplicationContext {
    private Class configClass;

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName) {
        return null;
    }
}
