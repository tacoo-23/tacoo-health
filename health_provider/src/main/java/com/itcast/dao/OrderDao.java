package com.itcast.dao;

import com.itcast.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

public interface OrderDao {

    Long findByCondition(Order order);

    void addOrder(Order order);

    Map findOrderInfoMessageById(Integer orderId);
}
