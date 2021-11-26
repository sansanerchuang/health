package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/23 - 11 -23 -18:14
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface OrderDao {
    public void add(Order order);

    public List<Order> findByCondition(Order order);

    Map findById4Detail(Integer id);
}
