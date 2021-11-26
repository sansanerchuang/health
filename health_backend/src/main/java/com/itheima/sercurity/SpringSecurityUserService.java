package com.itheima.sercurity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sun.security.util.Password;

import java.util.ArrayList;
import java.util.Set;

/**
 * @Auther:liyang
 * @Date:2021/11/26 - 11 -26 -16:23
 * @Description:com.itheima.sercurity
 * @Version:1.0
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByName(username);
        //判断用户名和用户输入的userName相同。
        if (user == null) {
            return null;

        }
        Set<Role> roles = user.getRoles();
        ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            System.out.println(role.getKeyword());
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                System.out.println(permission.getKeyword());
            }
        }
        //拿到的是加密之后的密码，之后框架拿着和input进行比较。
        System.out.println(user.getPassword());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
    }
}
