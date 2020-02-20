package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisConstant;
import com.itcast.entity.Result;
import com.itcast.service.UserService;
import com.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private UserService userService;

    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring-security完成认证后,会将当前用户信息保存到框架提供的上下文对象中,基于session实现的
        //获取上下文对象->获取认证信息对象->获取
        //该user是框架的user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user!=null){
            com.itcast.pojo.User loginUser = userService.findUserByUsername(user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,loginUser);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }

    //上传图片
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //按原始文件名上传容易重名
        String originalFilename = imgFile.getOriginalFilename();//26854587567.jpg
        int index = originalFilename.lastIndexOf(".");
        String fileName = originalFilename.substring(index);
//        System.out.println(fileName);
        String uploadFileName = UUID.randomUUID().toString()+fileName;

        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),uploadFileName);
            //往redis里添加上传图片名称信息
            jedisPool.getResource().sadd(RedisConstant.USER_PIC_RESOURCES,uploadFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.UPLOAD_SUCCESS,uploadFileName);
    }
    @RequestMapping("/getUserMessage")
    public Result getUserMessage(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            com.itcast.pojo.User loginUser = userService.findUserByUsername(user.getUsername());
            return new Result(true,MessageConstant.GET_USER_SUCCESS,loginUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,MessageConstant.GET_USER_FAIL);
    }
    @RequestMapping("/editUser")
    public Result editUser(@RequestBody com.itcast.pojo.User user,String oldImg){
        try {
            userService.editUser(user,oldImg);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
    }
}
