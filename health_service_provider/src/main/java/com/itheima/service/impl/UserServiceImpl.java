package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther:liyang
 * @Date:2021/11/26 - 11 -26 -16:38
 * @Description:com.itheima.service.impl
 * @Version:1.0
 */
@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    //实现user的查找以及role的封装，还有permission的封装
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public User findUserByName(String userName) {

        User user = userDao.findUserByName(userName);
        //查询完user表之后还要查询role表和user_role表。
        if (user == null) {
            return null;
        }
        Integer uid = user.getId();
        Set<Role> roles = roleDao.findRoleByUserId(uid);
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //查询role_id对应的Permission。
                Integer rid = role.getId();
                Set<Permission> permissions = permissionDao.findPermissionsByRid(rid);
                if (permissions != null && permissions.size() > 0) {
                    //将permission封装到role中。
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
