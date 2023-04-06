package com.wanghui.blog.controller;

import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.entity.Menu;
import com.wanghui.blog.entity.Role;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.BlogBackGroundLoginService;
import com.wanghui.blog.service.MenuService;
import com.wanghui.blog.service.RoleService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.util.SecurityUtils;
import com.wanghui.blog.vo.AdminUserInfoVo;
import com.wanghui.blog.vo.MenuVo;
import com.wanghui.blog.vo.RouterVo;
import com.wanghui.blog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 *  博客后台系统登录相关处理类
 * @Author 王辉
 * @Create 2023/4/3 17:51
 * @Version 1.0
 */
@RestController
public class BlogBackGroundLoginController {
    @Autowired
    private BlogBackGroundLoginService backGroundLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 博客后台登录接口
     * */
    @PostMapping("/user/login")
    @SelfDefinedSystemLog(BusinessName="博客后台登录接口")
    public ResponseResult login(@RequestBody User user){
        return backGroundLoginService.login(user);
    }

    /**
     * 博客后台用户权限接口
     * */
    @GetMapping("/getInfo")
    @SelfDefinedSystemLog(BusinessName="博客后台用户权限接口")
    public ResponseResult getInfo(){
        //获取当前登录的用户信息
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据用户id查询权限信息(menu表中的perms字段)
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询用户角色信息(roles表中的role_key字段)
        List<String> roles = roleService.selectRoleByUserId(loginUser.getUser().getId());
        //将用户信息转化为UserInfoVo类型的对象
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //封装数据到响应对象
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roles,userInfoVo);
        //返回数据
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 博客后台路由接口
     * */
    @GetMapping("/getRouters")
    @SelfDefinedSystemLog(BusinessName="博客后台路由接口")
    public ResponseResult getRouters(){
        //获取当前登录的用户信息
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户id
        Long userId = loginUser.getUser().getId();
        //查询menu 结果是tree的形式
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //将 menus 封装到 RouterVo 对象中
        RouterVo routerVo = new RouterVo(menus);
        //返回分装后的对象
        return ResponseResult.okResult(routerVo);
    }

    /**
     * 博客后台退出登录接口
     * */
    @PostMapping("/user/logout")
    @SelfDefinedSystemLog(BusinessName="博客后台退出登录接口")
    public ResponseResult loginOut(){
        return backGroundLoginService.loginOut();
    }
}
