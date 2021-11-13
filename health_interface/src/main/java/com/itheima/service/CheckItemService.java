package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @Auther:liyang
 * @Date:2021/11/12 - 11 -12 -10:20
 * @Description:com.itheima.service
 * @Version:1.0
 */
//服务接口
public interface CheckItemService {
    public void add(CheckItem checkItem);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    CheckItem selectById(Integer id);

    void editCheckItem(CheckItem checkItem);

    List<CheckItem> findAll();
}
