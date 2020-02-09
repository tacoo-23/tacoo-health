package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] ids) throws Exception;

    PageResult findByPage(QueryPageBean queryPageBean);

    List<Integer> findCheckItemIds(Integer id) throws Exception;

    void edit(CheckGroup checkGroup, Integer[] checkitemIds) throws Exception;

    void deleteGroupById(Integer id) throws Exception;

    List<CheckGroup> findAll() throws Exception;
}
