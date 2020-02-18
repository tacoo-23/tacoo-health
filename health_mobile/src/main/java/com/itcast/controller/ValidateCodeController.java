package com.itcast.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.utils.SMSUtils;
import com.itcast.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //发送体检预约验证码
    @RequestMapping("/sendOrderValidateCode")
    public Result sendOrderValidateCode(String telephone){
        //获取验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //发送验证码
        try {
            SMSUtils.sendShortMessage("SMS_183245652",telephone,validateCode.toString());
            //将验证码存入redis,并五分钟自动删除,因为五分钟自动删除,时效短,放入redis更好
            //key的名称用手机号+用途代号来做,因为登录注册预约都会用到验证码
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,3000,validateCode.toString());

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @RequestMapping("/sendLoginValidateCode")
    public Result sendLoginValidateCode(String telephone){
        //获取一个随机的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            //发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
            //验证码存入redis,五分钟自动销毁
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,3000,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
