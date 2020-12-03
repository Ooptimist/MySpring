package com.zc.demo;

import com.zc.spring.MySpringApplicationContext;

public class Test {
    public static void main(String[] args) {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(AppConfig.class);

        Object userService = mySpringApplicationContext.getBean("userService");
        System.out.println(userService);
    }
}
