package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * @Auther:liyang
 * @Date:2021/11/26 - 11 -26 -16:41
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface UserDao {
    User findUserByName(String userName);
}
