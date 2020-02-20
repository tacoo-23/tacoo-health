package com.itcast.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.pojo.Permission;
import com.itcast.pojo.Role;
import com.itcast.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;


    //根据用户名查询用户信息,(这样就会要求用户名要唯一)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();



        User user = userService.findUserByUsername(username);
        if (user==null){
            return null;
        }
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> list = new ArrayList<>();
        if (roles!=null){
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                if (permissions!=null){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}
