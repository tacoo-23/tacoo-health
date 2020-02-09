package com.itcast.dao;

import com.github.pagehelper.Page;
import com.itcast.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSmIdAndCgId(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> findAll();

    Setmeal findSetmealById(Integer setmealId);

    List<Integer> findcheckGroupsBySid(Integer setmealId);

    void editSetMEAL(Setmeal setmeal);
}
