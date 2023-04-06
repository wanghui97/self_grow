package com.wanghui.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/5 15:18
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private Long roleId;
    //角色状态（0正常 1停用）
    private String status;
}
