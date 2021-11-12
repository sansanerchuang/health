package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

/**
 * @Auther:liyang
 * @Date:2021/11/12 - 11 -12 -10:31
 * @Description:com.itheima.service
 * @Version:1.0
 */
//mybatis的动态代理方式产生实现类
public interface CheckItemDao {
    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String query);

    public void deleteById(Integer id);

    public long findCountByCheckItemId(Integer checkItemId);

    CheckItem selectById(Integer id);

    void editCheckItem(CheckItem checkItem);
}
