package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    void add(CheckItem checkItem) throws Exception;

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteCheckItemById(Integer id) throws Exception;

    void editOne(CheckItem checkItem) throws Exception;

    List<CheckItem> findAll() throws Exception;
}
