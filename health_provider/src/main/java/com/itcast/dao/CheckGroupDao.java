package com.itcast.dao;

import com.github.pagehelper.Page;
import com.itcast.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void addCheckGroupIdAndCheckItemsId(Map<String, Integer> map);

    Page<CheckGroup> findByPage(String queryString);

    List<Integer> findCheckItemIds(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupIdAndCheckItemsId(Integer id);

    void deleteGroupById(Integer id);

    List<CheckGroup> findAll();

    Long findCountById(Integer id);
}
