package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * @Auther:liyang
 * @Date:2021/11/14 - 11 -14 -20:03
 * @Description:com.itheima.controller
 * @Version:1.0
 */
@RequestMapping("/setMeal")
@RestController
public class SetMealController {
    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;

    //图片上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            String oriFileName = imgFile.getOriginalFilename();
            int lastIndex = oriFileName.lastIndexOf(".");
            String substring = oriFileName.substring(lastIndex - 1);
            String fileName = UUID.randomUUID() + substring;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkitemIds) {
        //将checkGroup的信息加入到t_checkGroup中
        System.out.println(setmeal);
        try {
            setMealService.add(setmeal, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //将integers的值的

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean pageBean) {
        PageResult pageResult = setMealService.findPage(pageBean.getCurrentPage(), pageBean.getPageSize(), pageBean.getQueryString());
        return pageResult;
    }

}
