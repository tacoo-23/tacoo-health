package com.itcast.dao;

import com.github.pagehelper.Page;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryPageString);

    void deleteById(Integer id) throws Exception;

    void editOne(CheckItem checkItem);

    List<CheckItem> findAll();

    Long findById(Integer id);
}
