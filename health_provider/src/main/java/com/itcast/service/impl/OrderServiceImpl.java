package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.constant.MessageConstant;
import com.itcast.dao.MemberDao;
import com.itcast.dao.OrderDao;
import com.itcast.dao.OrderSettingDao;
import com.itcast.entity.Result;
import com.itcast.pojo.Member;
import com.itcast.pojo.Order;
import com.itcast.pojo.OrderSetting;
import com.itcast.service.OrderService;
import com.itcast.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result submit(Map map) throws Exception {
        //1.检查预约日期是否有设置预约
        String orderDateStr = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(orderDateStr);
        //根据日期查询用户预约日期当天是否有设置预约,不查count是因为后面要用到orderSetting
        OrderSetting orderSetting=orderSettingDao.findOrderSetting(orderDate);
        System.out.println(orderSetting);
        if (orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查是否预约已满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations>=number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }


        String idCard = (String) map.get("idCard");
        //根据手机号查询会员用户
        Member member=memberDao.findMemberByIdCard(idCard);
        String setmealIdStr = (String) map.get("setmealId");
        //3.检查是否是同一个人同一天重复预约同一个套餐
        if (member!=null){
            //member不为null,说明已经注册过会员,也就是预约过
            Order order = new Order(member.getId(),orderDate,Integer.parseInt(setmealIdStr));

            Long count = orderDao.findByCondition(order);
            //根据日期,套餐id和会员id查找到了一个以上,说明已经预约了,那么本次预约是重复预约
            if (count>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }else {
            //4.检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            //新用户第一次预约,注册会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard(idCard);
            member.setPhoneNumber((String) map.get("telephone"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //5.预约成功
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setSetmealId(Integer.parseInt(setmealIdStr));
        order.setOrderDate(orderDate);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType((String) map.get("orderType"));
        orderDao.addOrder(order);
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.updateReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findOrderInfoMessageById(Integer orderId) throws Exception {
        Map map = orderDao.findOrderInfoMessageById(orderId);
        if (map!=null){
            Date date = (Date) map.get("orderDate");
            //让页面显示yyyy-MM-dd的字符串形式,否则日期格式后面还有00:00:00
            map.put("orderDate",DateUtils.parseDate2String(date));
        }

        return map;
    }
}
