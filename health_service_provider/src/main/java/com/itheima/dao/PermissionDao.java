package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * @Auther:liyang
 * @Date:2021/11/26 - 11 -26 -16:47
 * @Description:com.itheima.dao
 * @Version:1.0
 */
public interface PermissionDao {
    Set<Permission> findPermissionsByRid(Integer rid);
}
