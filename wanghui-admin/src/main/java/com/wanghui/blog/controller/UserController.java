package com.wanghui.blog.controller;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.dto.TagListDto;
import com.wanghui.blog.dto.UserDto;
import com.wanghui.blog.dto.UserUpdateDto;
import com.wanghui.blog.entity.Tag;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import com.wanghui.blog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/6 18:35
 * @Version 1.0
 */
@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询分页后的用户信息列表
     * @param pageNum: 页码
     * @param pageSize: 每页条数
     * @param userName：用户名
     * @param phonenumber：手机号
     * @param status:状态
     * */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName = "查询分页后的标签信息列表")
    public ResponseResult<PageVo> selectUserListPage(Integer pageNum, Integer pageSize,String userName,String phonenumber,String status){
        return userService.selectUserListPage(pageNum,pageSize,userName,phonenumber,status);
    }

    /**
     * 新增用户信息
     * */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName = "新增用户信息")
    public ResponseResult save(@RequestBody UserDto userDto){
        return userService.saveUserInfo(userDto);
    }

    /**
     * 删除用户信息
     * */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "删除用户信息")
    public ResponseResult delete(@PathVariable("id") String userId){
        String[] userIdArray = userId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> userIds = Arrays.asList(userIdArray).stream()
                .map(userListId -> Long.valueOf(userListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(userService.removeByIds(userIds));
    }

    /**
     * 根据用户id查询角色信息
     * @param userId: 用户id
     * */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "根据用户id查询角色信息")
    public ResponseResult selectUserRoleList(@PathVariable("id") Long userId){
        return userService.selectUserRoleList(userId);
    }

    /**
     * 修该用户信息
     * */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName = "修该用户信息")
    public ResponseResult updateUserAndRoleById(@RequestBody UserUpdateDto userUpdateDto){
        return userService.updateUserAndRoleById(userUpdateDto);
    }

    /**
     * 修改用户状态
     * */
    @PutMapping("/changeStatus")
    @SelfDefinedSystemLog(BusinessName = "修改用户状态")
    public ResponseResult updateLinkById(@RequestBody UserVo userVo){
        User user = new User();
        user.setId(userVo.getUserId());
        user.setStatus(userVo.getStatus());
        boolean updateFlag = userService.updateById(user);
        if(!updateFlag){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

}
