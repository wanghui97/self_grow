package com.wanghui.blog.controller;

import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.BlogReceptionLoginService;
import com.wanghui.blog.util.ResponseResult;
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
    public ResponseResult login(@RequestBody User user){
        return blogReceptionLoginService.login(user);
    }
    /**
     * 退出登录接口
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult loginOut(){
        return blogReceptionLoginService.loginOut();
    }
}
