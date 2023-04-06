package com.wanghui.blog.service;

import com.wanghui.blog.entity.User;
import com.wanghui.blog.util.ResponseResult;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/3 18:01
 * @Version 1.0
 */
public interface BlogBackGroundLoginService {
    ResponseResult login(User user);

    ResponseResult loginOut();
}
