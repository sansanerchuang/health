package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/13 - 11 -13 -19:37
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface SetMealDao {

    void add(Setmeal setmeal);

    void setSetMealAndCheckGroup(Map map);

    Page<Setmeal> queryByCondition(String queryString);

//    Page<CheckGroup> queryByCondition(String queryString);


}
