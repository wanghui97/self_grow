package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.mapper.UserMapper;
import com.wanghui.blog.service.MenuService;
import com.wanghui.blog.util.CodeLibraryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * ClassName: UserDeatailsServiceImpl
 * Package: com.wanghui.blog.service.impl
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/27 11:09
 * @Version 1.0
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //判断是否查询到
        if(Objects.isNull(user)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        if(user.getType().equals(CodeLibraryUtil.ADMIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //返回用户信息
        return new LoginUser(user,null);
    }
}
