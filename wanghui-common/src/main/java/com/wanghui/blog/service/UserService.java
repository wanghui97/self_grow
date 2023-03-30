package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.util.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author wanghui
 * @since 2023-03-27 09:13:55
 */
public interface UserService extends IService<User> {
    ResponseResult queryUser();

    ResponseResult saveSelfInfo(User user);

    ResponseResult updateSelfInfo(User user);
}
