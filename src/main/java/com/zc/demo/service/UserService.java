package com.zc.demo.service;

import com.zc.spring.Autowired;
import com.zc.spring.Component;

@Component("userService")
public class UserService {

    @Autowired
    private User user;
}
