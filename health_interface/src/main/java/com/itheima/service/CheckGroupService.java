package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @Auther:liyang
 * @Date:2021/11/13 - 11 -13 -19:28
 * @Description:com.itheima.service
 * @Version:1.0
 */

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] integers);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findById(Integer id);

    Integer[] findCheckitems(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemids);

    List<CheckGroup> findAll();
}
