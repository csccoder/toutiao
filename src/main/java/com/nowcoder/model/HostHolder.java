package com.nowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<User> threadLocal=new ThreadLocal<User>();

    public void set(User user){
        threadLocal.set(user);
    }

    public User get(){
        return threadLocal.get();
    }

    public void clear(){
        threadLocal.remove();
    }
}
