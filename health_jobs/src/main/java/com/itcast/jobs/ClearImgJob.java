package com.itcast.jobs;

import com.itcast.constant.RedisConstant;
import com.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //计算差值的,返回set集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //删除多余的照片记录
        if (set!=null){
            for (String fileName : set) {
                QiniuUtils.deleteFileFromQiniu(fileName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            }
        }
        jedisPool.getResource().sdiff(RedisConstant.USER_PIC_RESOURCES,RedisConstant.USER_PIC_DB_RESOURCES);
        if (set!=null){
            for (String fileName : set) {
                QiniuUtils.deleteFileFromQiniu(fileName);
                jedisPool.getResource().srem(RedisConstant.USER_PIC_RESOURCES,fileName);
            }
        }

    }
}
