package com.zc.demo.service;

import com.zc.spring.BeanPostProcessor;
import com.zc.spring.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println(beanName+"初始化之前");
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println(beanName+"初始化之后");
        return bean;
    }
}
