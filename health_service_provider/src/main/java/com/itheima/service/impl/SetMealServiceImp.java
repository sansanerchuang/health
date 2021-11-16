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
import java.util.List;
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
    //只有这样添加的数据被加入到了redis中@

    private void savePic2Redis(String pic) {
        //添加到set集合中，把中数据库中查询出来的img
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> pages = setMealDao.queryByCondition(queryString);
        if (pages != null && pages.size() > 0) {
            for (Setmeal setmeal : pages.getResult()) {
                savePic2Redis(setmeal.getImg());
            }
        }

        return new PageResult(pages.getTotal(), pages.getResult());
    }

    //    返回所有的套餐信息
    @Override
    public List<Setmeal> getSetMeals() {
        return setMealDao.getSetMeals();
    }


    @Override
    public Setmeal findSetMealById(int id) {
        return setMealDao.findById(id);
    }


//    @Override
//    public Setmeal findById(int id) {
//        return setMealDao.findById(id);
//    }

    //    返回单个套餐的是详细信息，报告检查组，检查项

    private void setSetMealAndCheckGroup(Setmeal setmeal, Integer[] integers) {
        Map map = new HashMap<>();
        for (Integer id : integers) {
            map.put("setMealId", setmeal.getId());
            map.put("checkGroupId", id);
            setMealDao.setSetMealAndCheckGroup(map);
        }
    }


}
