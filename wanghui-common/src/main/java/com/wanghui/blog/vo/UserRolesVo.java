package com.wanghui.blog.vo;

import com.wanghui.blog.entity.Role;
import com.wanghui.blog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/6 19:26
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesVo {

    //用户权限id列表
    private List<Long> roleIds;
    //用户权限列表
    private List<Role> roles;
    //用户详细信息
    private User user;
}
