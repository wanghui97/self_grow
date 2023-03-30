package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.mapper.UserMapper;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户表(User)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-27 09:13:55
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseResult login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getUserName,user.getUserName());
        lambdaQueryWrapper.eq(User::getPassword,user.getPassword());
        User userLogin = userMapper.selectOne(lambdaQueryWrapper);
        if(userLogin==null){
           return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryUser() {
        //从SecurityContextHolder中获取userid
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
        //查询客户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult saveSelfInfo(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //密码加密处理
        String encode = passwordEncoder.encode(user.getPassword());
        //将加密后的密码存入到user对象
        user.setPassword(encode);
        //保存用户信息到数据库
        int count = userMapper.insert(user);
        if(count!=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateSelfInfo(User user) {
        int count = userMapper.updateById(user);
        if(count!=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,nickName);
        return count(wrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return count(wrapper) > 0;
    }
}
