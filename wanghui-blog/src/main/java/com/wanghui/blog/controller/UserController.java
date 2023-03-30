package com.wanghui.blog.controller;


import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.ResponseResult;
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
     * @return 所有数据
     */
    @GetMapping("/userInfo")
    public ResponseResult queryUser() {
        return userService.queryUser();
    }

    /**
     * 博客前台保存个人信息
     * @return 保存状态
     */
    @PutMapping("/userInfo")
    public ResponseResult updateSelfInfo(@RequestBody User user) {
        return userService.updateSelfInfo(user);
    }

    /**
     * 博客前台注册个人信息
     * @return 保存状态
     */
    @PostMapping("/register")
    public ResponseResult saveSelfInfo(@RequestBody User user) {
        return userService.saveSelfInfo(user);
    }
}

