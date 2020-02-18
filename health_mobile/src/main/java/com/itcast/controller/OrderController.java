package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.Order;
import com.itcast.service.OrderService;
import com.itcast.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //取出电话号码和验证码
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        //从缓存中取出验证码
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (!validateCode.equals(redisValidateCode)){
            //验证码不匹配,或者验证码已过期(redisValidateCode为null)
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码已通过
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Result result =null;
        try {
             result=orderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
        if (result.isFlag()){
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    @RequestMapping("/findOrderInfoMessageById")
    public Result findOrderInfoMessageById(Integer orderId){
        try {
            Map map=orderService.findOrderInfoMessageById(orderId);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
