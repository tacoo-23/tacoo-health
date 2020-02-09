package com.itcast.service;

import com.itcast.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void upload(List<OrderSetting> list) throws Exception;

    List<Map> findOrderByDate(String currentMonth);

    void order(OrderSetting orderSetting) throws Exception;
}
