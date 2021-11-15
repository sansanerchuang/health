package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/15 - 11 -15 -17:24
 * @Description:com.itheima.controller
 * @Version:1.0
 */
///ordersetting/upload.do"
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderService;

    @RequestMapping("/upload")
    //对于批量处理的表单，
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        //将数据从excel表格中读取出来，使用我们的工具类
        try {
//              返回的一个行的集合
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<>();
//    遍历行
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    //                          批量的数据取出来加入到安排表中集合中
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettings.add(orderSetting);
                }
            }
            orderService.upload(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


//    getOrderSettingByMonth.do?date

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> orderSettingByMoth = orderService.getOrderSettingByMoth(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderSettingByMoth);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    //我们输入日期，以及要修改的数字，
    @RequestMapping("/editNumberByDate")
    public Result getEditNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);

        }
    }
}
