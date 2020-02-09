package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.dao.CheckItemDao;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.pojo.CheckItem;
import com.itcast.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) throws Exception {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page=checkItemDao.findPage(queryPageBean.getQueryString());

        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public void deleteCheckItemById(Integer id) throws Exception {
        //查关联表
        Long count = checkItemDao.findById(id);
        if(count>0){
            //和检查组有关联了
            throw new RuntimeException();
        }else{
            checkItemDao.deleteById(id);
        }

    }

    @Override
    public void editOne(CheckItem checkItem) throws Exception {
        checkItemDao.editOne(checkItem);
    }

    @Override
    public List<CheckItem> findAll() throws Exception {
        return checkItemDao.findAll();
    }
}
