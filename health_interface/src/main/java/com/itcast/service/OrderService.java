package com.itcast.service;

import com.itcast.entity.Result;

import java.util.Map;

public interface OrderService {
    Result submit(Map map) throws Exception;

    Map findOrderInfoMessageById(Integer orderId) throws Exception;
}
