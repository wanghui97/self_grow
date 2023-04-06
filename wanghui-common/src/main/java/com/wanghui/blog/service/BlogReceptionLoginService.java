package com.wanghui.blog.service;

import com.wanghui.blog.entity.User;
import com.wanghui.blog.util.ResponseResult;

/**
 * Package: com.wanghui.blog.controller
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/27 10:16
 * @Version 1.0
 */
public interface BlogReceptionLoginService {
    ResponseResult login(User user);

    ResponseResult loginOut();
}
