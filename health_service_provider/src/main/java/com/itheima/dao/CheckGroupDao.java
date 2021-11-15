package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @Auther:liyang
 * @Date:2021/11/13 - 11 -13 -19:37
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupIdAndCheckitem(Map map);

    Page<CheckGroup> queryByCondition(String queryString);

    CheckGroup findById(Integer id);

    Integer[] findCheckitemIds(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    List<CheckGroup> findAll();
}
