package com.itcast.dao;

import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.Order;
import com.itcast.pojo.SetmealData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    Long findByCondition(Order order);

    void addOrder(Order order);

    Map findOrderInfoMessageById(Integer orderId);

    List<SetmealData> getSetmealData();

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findOrderNumberAfterDate(String firstDayOfWeek);

    Integer findVisitsNumberAfterDate(String firstDayOfWeek);

    List<HotSetmeal> findHotSetmeals();
}
