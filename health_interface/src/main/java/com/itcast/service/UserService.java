package com.itcast.service;

import com.itcast.pojo.User;


public interface UserService {
    User findUserByUsername(String username);


    void editUser(User user, String oldImg) throws Exception;
}
