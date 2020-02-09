package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.dao.OrderSettingDao;
import com.itcast.pojo.OrderSetting;
import com.itcast.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> list) throws Exception {
        //防止用户多次导入同样的数据,需要先查询数据库
        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                Long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
                if (count>0){
                    //有记录了,那么以后面导入的为准,所以需要更改
                    orderSettingDao.updateByDate(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> findOrderByDate(String date) {//yyyy-MM
        Map<String,String> map = new HashMap<String, String>();
        map.put("firstDay",date+"-1");
        map.put("lastDay",date+"-31");//一个月最多31天
        List<OrderSetting> orderSettingList = orderSettingDao.findOrder(map);
//        Map m = new HashMap();
        List<Map> list = new ArrayList<>();
        if(orderSettingList!=null){

            for (OrderSetting orderSetting : orderSettingList) {
                //map若在循环外,值会被覆盖,就算放进list集合,由于是同一个对象
                //list集合中的map中的值也是被不断覆盖
                Map m = new HashMap();
                m.put("date",orderSetting.getOrderDate().getDate());
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                list.add(m);
            }
        }

        return list;
    }

    @Override
    public void order(OrderSetting orderSetting) throws Exception {
        //在数据库有记录的基础上设置是更改,没有便是添加
//        String newDate = date.replaceAll("-", "/");
        //因为数据库的日期格式是按表格中的yyyy/mm/dd格式导入的
        //按照yyyy-mm-dd格式是无法匹配数据库的日期格式
        Long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
//        System.out.println(new Date(newDate)+"  "+num);
        if (count>0){
            orderSettingDao.updateByDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
