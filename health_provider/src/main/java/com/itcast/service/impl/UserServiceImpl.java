package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.constant.RedisConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.dao.UserDao;
import com.itcast.pojo.User;
import com.itcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.JedisPool;

@Service(interfaceClass = UserService.class)
@Controller
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public void editUser(User user, String oldImg) throws Exception {
        //用户名需要唯一
        userDao.editUser(user);
        if (oldImg!=null){
            //从缓存中先删除旧照片
            jedisPool.getResource().srem(RedisConstant.USER_PIC_DB_RESOURCES,oldImg);
        }
        jedisPool.getResource().sadd(RedisConstant.USER_PIC_DB_RESOURCES,user.getImg());
    }
}
