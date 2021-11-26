package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/23 - 11 -23 -16:20
 * @Description:com.itheima.controller
 * @Version:1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //要检验验证码
        String telephone = (String) map.get("telephone");
        String mCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //判断map中的code和我们的map取出来的是不是一致
        String uCode = map.get("validateCode").toString();
        if (mCode == null && !mCode.equals(uCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()) {
//            //发送成功的短信
//            //预约成功，发送短信通知
//            String orderDate = (String) map.get("orderDate");
//            try {
//                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
        }
        return result;
    }

    //   查询套餐信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
