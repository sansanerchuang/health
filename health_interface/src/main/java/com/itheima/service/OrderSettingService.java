package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/15 - 11 -15 -17:31
 * @Description:com.itheima.service
 * @Version:1.0
 */
public interface OrderSettingService {
    void upload(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMoth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
