package com.wanghui.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * ClassName: LoginUser
 * Package: com.wanghui.blog.entity
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/27 11:30
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {
    @Autowired
    private User user;
    //权限信息列表
    private List<String> permissions;
    //返回一个权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //获取密码
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //应用内唯一的用户名
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //账户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账户是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //凭证是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
