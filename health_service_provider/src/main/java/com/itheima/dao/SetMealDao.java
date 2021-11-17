package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/13 - 11 -13 -19:37
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface SetMealDao {
    //添加套餐信息
    void add(Setmeal setmeal);

    //修改套餐信息
    void setSetMealAndCheckGroup(Map map);

    //分页查询
    Page<Setmeal> queryByCondition(String queryString);

    //查询所有的套餐信息
    List<Setmeal> getSetMeals();

    Setmeal findById(int id);

    List<Setmeal> findAll();

//    Page<CheckGroup> queryByCondition(String queryString);


}
