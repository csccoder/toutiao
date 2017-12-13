package com.nowcoder.service;

import com.nowcoder.model.User;

import java.util.Map;

public interface UserService {
    User selectUserById(int userId);

    Map<String,String> register(String username, String password, int remember);

    Map<String,String> login(String username, String password, int remember);

    String addLoginTicket(User user, int remember);

    void logout(String ticket);
}
