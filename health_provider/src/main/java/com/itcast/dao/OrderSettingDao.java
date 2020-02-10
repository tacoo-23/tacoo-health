package com.itcast.dao;

import com.itcast.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    Long findCountByDate(Date orderDate);

    void updateByDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> findOrder(Map<String, String> map);

    OrderSetting findOrderSetting(Date orderDate);

    void updateReservationsByOrderDate(OrderSetting orderSetting);
}
