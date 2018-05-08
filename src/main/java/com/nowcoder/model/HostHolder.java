package com.nowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //使用ThreadLocal进行请求上下文的设置，ThreadLocal是线程安全的每个线程获取的是本线程所对应的值
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
