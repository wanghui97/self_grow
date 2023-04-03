package com.wanghui.blog.controller;


import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;

/**
 * 用户表(User)表控制层
 *
 * @author wanghui
 * @since 2023-03-27 09:13:55
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 用户信息查询
     *  该接口不用传参，可以直接在token中获取用户id信息，降低风险
     * @return 所有数据
     */
    @GetMapping("/userInfo")
    @SelfDefinedSystemLog(BusinessName="用户信息查询")
    @ApiOperation(value = "用户信息查询",notes = "用户信息查询")
    @ApiImplicitParams({
    })
    public ResponseResult queryUser() {
        return userService.queryUser();
    }

    /**
     * 博客前台保存个人信息
     * @return 保存状态
     */
    @PutMapping("/userInfo")
    @SelfDefinedSystemLog(BusinessName="博客前台保存个人信息")
    @ApiOperation(value = "保存客户信息",notes = "博客前台保存个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户信息")
    })
    public ResponseResult updateSelfInfo(@RequestBody User user) {
        return userService.updateSelfInfo(user);
    }

    /**
     * 博客前台注册个人信息
     * @return 保存状态
     */
    @PostMapping("/register")
    @SelfDefinedSystemLog(BusinessName="博客前台注册个人信息")
    @ApiOperation(value = "注册客户信息",notes = "博客前台注册个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户信息")
    })
    public ResponseResult saveSelfInfo(@RequestBody User user) {
        return userService.saveSelfInfo(user);
    }
}

