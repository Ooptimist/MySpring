package com.zc.demo;

import com.zc.demo.service.UserService;
import com.zc.spring.MySpringApplicationContext;

public class Test {
    public static void main(String[] args) {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(AppConfig.class);

        UserService userService = (UserService) mySpringApplicationContext.getBean("userService");
        System.out.println(userService.getUser());
        System.out.println(userService.getBeanName());
    }
}
