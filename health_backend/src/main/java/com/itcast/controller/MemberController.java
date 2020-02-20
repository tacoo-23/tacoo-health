package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    @RequestMapping("/findPage")
    public Object findPage(@RequestBody QueryPageBean pageBean){
        PageResult pageResult=memberService.findPage(pageBean);
        Map<String,Object> map = new HashMap<>();
        map.put("rows",pageResult.getRows());
        map.put("total",pageResult.getTotal());
        Object o = JSON.toJSON(map);
        return o;
//        return pageResult;
    }
}
