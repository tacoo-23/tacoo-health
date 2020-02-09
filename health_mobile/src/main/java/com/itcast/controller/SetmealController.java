package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.Setmeal;
import com.itcast.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Setmeal> setmeals = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
    @RequestMapping("/findSetmealById")
    public Result findSetmealById(Integer setmealId){
        try {
            Setmeal setmeal=setmealService.findSetmealById(setmealId);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
