package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.dao.CheckGroupDao;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.CheckGroup;
import com.itcast.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add(CheckGroup checkGroup, Integer[] ids) throws Exception {
        //增加检查组,检查组的id是自增长的,所以需要增加后封装到checkGroup中
        checkGroupDao.add(checkGroup);

        //关联检查组和检查项
        if (ids!=null){
            Map<String,Integer> map = new HashMap<String, Integer>();
            for (Integer id : ids) {
                map.put("checkGroup_id",checkGroup.getId());
                map.put("checkItems_id",id);
                checkGroupDao.addCheckGroupIdAndCheckItemsId(map);
            }
        }
    }
    //分页查询
    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //开启分页助手
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<CheckGroup> page=checkGroupDao.findByPage(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public List<Integer> findCheckItemIds(Integer id) throws Exception {
        return checkGroupDao.findCheckItemIds(id);
    }
    //编辑检查组
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) throws Exception {
        checkGroupDao.edit(checkGroup);
        //先删除关联表中的记录
        checkGroupDao.deleteCheckGroupIdAndCheckItemsId(checkGroup.getId());
        if (checkitemIds!=null){
            Map<String,Integer> map = new HashMap<String, Integer>();
            for (Integer id : checkitemIds) {
                map.put("checkGroup_id",checkGroup.getId());
                map.put("checkItems_id",id);
                checkGroupDao.addCheckGroupIdAndCheckItemsId(map);
            }
        }
    }
    //删除检查组,暂时无需测试
    @Override
    public void deleteGroupById(Integer id) throws Exception {
        Long count = checkGroupDao.findCountById(id);
        if (count>0){
            throw new RuntimeException();
        }else{
            checkGroupDao.deleteCheckGroupIdAndCheckItemsId(id);
            checkGroupDao.deleteGroupById(id);
        }


    }

    @Override
    public List<CheckGroup> findAll() throws Exception {

        return checkGroupDao.findAll();
    }
}
