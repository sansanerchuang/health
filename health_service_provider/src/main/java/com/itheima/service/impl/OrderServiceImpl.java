package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/23 - 11 -23 -17:16
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Result submitOrder(Map map) throws Exception {
        //提交预约信息：
        //需要查看是预约有没有被设置，去查询orderSetting那表
        String time = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(time);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //需要查看预约人数有有满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //检查当前用户是否为会员，根据手机号判断,如果这个号码在memberDao的表中没有，就一定不是重复的
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        if (member != null) {
            //电话号码存贮过,证明可能是重复的，判断是不是重复的预约
            //把map中telephone，以及OrderDate，setmealId去查收order表，如果查出来了数据就是重复的
            Integer memberId = member.getId();
            Order order = new Order();
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
            List<Order> orders = orderDao.findByCondition(order);
            if (orders != null && orders.size() > 0) {
                //重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //没有存贮过,可以预约,或是会员
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //如果是第一次预约就添加会员
        if (member == null) {
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //预约成功之后，将预约成功的id传回到页面中
        //将会员和新用户的信息存到预约表中
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
