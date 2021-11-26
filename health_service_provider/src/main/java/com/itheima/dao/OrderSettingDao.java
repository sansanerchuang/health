package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/15 - 11 -15 -18:02
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface OrderSettingDao {
    public Integer findCountSetting(Date orderDate);

    void add(OrderSetting orderSetting);

    void editNumberByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> getOderSettingByMoth(Map map);

    OrderSetting findByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
