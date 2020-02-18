package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisConstant;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.entity.Result;
import com.itcast.pojo.Setmeal;
import com.itcast.service.SetmealService;
import com.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

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
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,uploadFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.UPLOAD_SUCCESS,uploadFileName);
    }

    //添加套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkGroupIds){
        try {
            setmealService.add(setmeal,checkGroupIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }


        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult= null;
        try {
            pageResult = setmealService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pageResult;
    }
    @RequestMapping("/findcheckGroupsBySid")
    public Result findcheckGroupsBySid(Integer setmealId){
        try {
            List<Integer> list = setmealService.findcheckGroupsBySid(setmealId);
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
    @RequestMapping("/editSetmeal")
    public Result editSetmeal(@RequestBody Setmeal setmeal,String oldImg,Integer[] checkgroupIds){
        try {
            setmealService.editSetmeal(setmeal,oldImg,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }
}
