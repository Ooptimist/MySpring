package com.zc.demo.service;

import com.zc.spring.*;

@Component("userService")
//@Scope("prototype")
//@Lazy
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private User user;

    private String beanName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public void afterPropertiesSet() {
        System.out.println(beanName + "进入afterPropertiesSet()方法");
    }
}
