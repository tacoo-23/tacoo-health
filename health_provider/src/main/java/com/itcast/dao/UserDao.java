package com.itcast.dao;

import com.itcast.pojo.User;

public interface UserDao {
    User findUserByUsername(String username);

    void editUser(User user);

    String findUserByUid(Integer userId);
}
