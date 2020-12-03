package com.zc.demo.service;

import com.zc.spring.Autowired;
import com.zc.spring.Component;
import com.zc.spring.Lazy;
import com.zc.spring.Scope;

@Component("userService")
//@Scope("prototype")
//@Lazy
public class UserService {

    @Autowired
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
