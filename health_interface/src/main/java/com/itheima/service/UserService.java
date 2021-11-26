package com.itheima.service;

import com.itheima.pojo.User;

/**
 * @Auther:liyang
 * @Date:2021/11/26 - 11 -26 -16:26
 * @Description:com.itheima.service
 * @Version:1.0
 */
public interface UserService {
    //查询User的全部信息，包括role,permission
    User findUserByName(String userName);
}
