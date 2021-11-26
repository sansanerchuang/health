package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/23 - 11 -23 -16:32
 * @Description:com.itheima.service
 * @Version:1.0
 */
public interface OrderService {
    Result submitOrder(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
