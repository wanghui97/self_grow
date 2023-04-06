package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.dto.UserDto;
import com.wanghui.blog.dto.UserUpdateDto;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.entity.Role;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.entity.UserRole;
import com.wanghui.blog.mapper.RoleMapper;
import com.wanghui.blog.mapper.UserMapper;
import com.wanghui.blog.mapper.UserRoleMapper;
import com.wanghui.blog.service.UserRoleService;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import com.wanghui.blog.vo.UserInfoVo;
import com.wanghui.blog.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-27 09:13:55
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;

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

    @Override
    public ResponseResult<PageVo> selectUserListPage(Integer pageNum, Integer pageSize, String userName, String phoneNumber, String status) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(StringUtils.hasText(userName),User::getUserName,userName);
        userLambdaQueryWrapper.eq(StringUtils.hasText(phoneNumber),User::getPhonenumber,phoneNumber);
        userLambdaQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> userPage = new Page<>(pageNum, pageSize);
        userPage = userMapper.selectPage(userPage,userLambdaQueryWrapper);
        PageVo pageVo = new PageVo(userPage.getRecords(), userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult saveUserInfo(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
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
        int insert = userMapper.insert(user);
        if(insert!=1){
            throw new RuntimeException("用户信息保存失败!");
        }
        List<UserRole> userRoles = userDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserRoleList(Long userId) {
        //查询用户信息
        User user = userMapper.selectById(userId);
        //查询该用户的角色关联信息
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleLambdaQueryWrapper);
        //获取该用户的角色id列表
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        //获取所有的角色信息列表
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, CodeLibraryUtil.STATUS_NORMAL);
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
        //根据角色id查询角色信息
        /*if(Objects.isNull(roleIds)){
            roles = roleMapper.selectBatchIds(roleIds);
        }*/
        //封装用户权限信息对象并返回
        UserRolesVo userRolesVo = new UserRolesVo(roleIds, roles, user);
        return ResponseResult.okResult(userRolesVo);
    }

    @Override
    public ResponseResult updateUserAndRoleById(UserUpdateDto userUpdateDto) {
        User user = BeanCopyUtils.copyBean(userUpdateDto, User.class);
        int updateCount = userMapper.updateById(user);
        if(updateCount!=1){
            throw new RuntimeException("用户信息更新失败！");
        }
        List<UserRole> userRoles = userUpdateDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        //删除用户角色原关联信息
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(userRoleLambdaQueryWrapper);
        //批量保存用户与角色新关联信息
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }
}
