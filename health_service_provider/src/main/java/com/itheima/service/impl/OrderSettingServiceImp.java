package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther:liyang
 * @Date:2021/11/15 - 11 -15 -17:33
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImp implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> orderSettings) {
        if (orderSettings != null && orderSettings.size() > 0) {
            //将数据插入到数据库中，但是要判断，日期只能插入一遍
            for (OrderSetting orderSetting : orderSettings) {
                //先去查询数据库，有没有这个日期，如果有的话就将数据覆盖
                Integer count = orderSettingDao.findCountSetting(orderSetting.getOrderDate());
                if (count != null && count > 0) {
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }
                //没有这个日期
                else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }

    }

    @Override
    public List<Map> getOrderSettingByMoth(String date) {
        //将date转换成是Date()
        Map Condition = new HashMap<>();
        String Begin = date + '-' + "1";
        String End = date + '-' + "31";
        Condition.put("begin", Begin);
        Condition.put("end", End);
        List<OrderSetting> oderSettingByMoth = orderSettingDao.getOderSettingByMoth(Condition);//参数格式为：2019-03
        List<Map> maps = new ArrayList<>();
//        对集合进行处理
        if (oderSettingByMoth != null && oderSettingByMoth.size() > 0) {
            for (OrderSetting orderSetting : oderSettingByMoth) {
                //安装我们需要回写的leftObj的json格式将数据回写到map集合当中
                Map stringStringMap = new HashMap<>();
//                { date: 1, number: 120, reservations: 1
//                获取日期类
                stringStringMap.put("date", orderSetting.getOrderDate().getDate());
//                获取number
                stringStringMap.put("number", orderSetting.getNumber());
//                获取reservations
                stringStringMap.put("reservations", orderSetting.getReservations());
                maps.add(stringStringMap);
            }
        }
        return maps;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        orderSettingDao.editNumberByOrderDate(orderSetting);
    }

}
