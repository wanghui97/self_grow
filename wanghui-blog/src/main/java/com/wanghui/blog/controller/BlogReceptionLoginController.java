package com.wanghui.blog.controller;

import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.BlogReceptionLoginService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: BlogReceptionLogin
 * Package: com.wanghui.blog.controller
 * Description:
 *  博客系统前台登录
 * @Author 王辉
 * @Create 2023/3/27 9:27
 * @Version 1.0
 */
@RestController
public class BlogReceptionLoginController {
    @Autowired
    private BlogReceptionLoginService blogReceptionLoginService;

    /**
     * 登录接口
     * @return 用户信息
     */
    @PostMapping("/login")
    @SelfDefinedSystemLog(BusinessName="用户登录")
    @ApiOperation(value = "用户登录",notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户信息")
    })
    public ResponseResult login(@RequestBody User user){
        return blogReceptionLoginService.login(user);
    }
    /**
     * 退出登录接口
     * @return
     */
    @PostMapping("/logout")
    @SelfDefinedSystemLog(BusinessName="退出登录")
    @ApiOperation(value = "退出登录",notes = "退出登录")
    @ApiImplicitParams({
    })
    public ResponseResult loginOut(){
        return blogReceptionLoginService.loginOut();
    }
}
