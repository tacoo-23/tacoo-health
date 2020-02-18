package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.entity.Result;
import com.itcast.pojo.CheckItem;
import com.itcast.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    //添加检查项
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
        Result result = null;
        try {
            checkItemService.add(checkItem);
            result = new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return result;
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }
    //删除检查项
    @RequestMapping("/deleteCheckItemById")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result deleteCheckItemById(Integer id){
        Result result = null;
        try {
            checkItemService.deleteCheckItemById(id);
            result=new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result=new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return result;
    }

    //编辑检查项
    @RequestMapping("/editOne")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result editOne(@RequestBody CheckItem checkItem){
        try {
            checkItemService.editOne(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItems=checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
