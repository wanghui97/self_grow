package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.dto.UserDto;
import com.wanghui.blog.dto.UserUpdateDto;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;


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

    ResponseResult<PageVo> selectUserListPage(Integer pageNum, Integer pageSize, String userName, String phoneNumber, String status);

    ResponseResult saveUserInfo(UserDto userDto);

    ResponseResult selectUserRoleList(Long userId);

    ResponseResult updateUserAndRoleById(UserUpdateDto userUpdateDto);
}
