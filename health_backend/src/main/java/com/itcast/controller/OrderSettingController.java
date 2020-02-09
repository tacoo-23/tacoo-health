package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.OrderSetting;
import com.itcast.service.OrderSettingService;
import com.itcast.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result uploadExcel(MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
            //导入表格数据到数据库,将list集合中的数组元素封装到orderSettig对象中
            //因为对象属性和列是可以一一对应的,方便操作
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                orderSettings.add(orderSetting);
            }
            orderSettingService.upload(orderSettings);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/findOrderByDate")
    public Result findOrderByDate(String date){
//        System.out.println(currentMonth);
        try {
            List<Map> list = orderSettingService.findOrderByDate(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
    @RequestMapping("/order")
    public Result order(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.order(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
