package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/14 - 11 -14 -20:32
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImp implements SetMealService {
    @Autowired
    SetMealDao setMealDao;
    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] integers) {
        //向两长表中插入数据
        setMealDao.add(setmeal);
        setSetMealAndCheckGroup(setmeal, integers);
        savePic2Redis(setmeal.getImg());
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> pages = setMealDao.queryByCondition(queryString);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    private void setSetMealAndCheckGroup(Setmeal setmeal, Integer[] integers) {
        Map map = new HashMap<>();
        for (Integer id : integers) {
            map.put("setMealId", setmeal.getId());
            map.put("checkGroupId", id);
            setMealDao.setSetMealAndCheckGroup(map);
        }
    }


}
