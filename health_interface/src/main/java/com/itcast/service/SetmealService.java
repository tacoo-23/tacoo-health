package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds) throws Exception;

    PageResult findPage(QueryPageBean queryPageBean) throws Exception;

    List<Setmeal> findAll() throws Exception;

    Setmeal findSetmealById(Integer setmealId) throws Exception;

    List<Integer> findcheckGroupsBySid(Integer setmealId) throws Exception;

    void editSetmeal(Setmeal setmeal, String oldImg, Integer[] checkgroupIds) throws Exception;
}
