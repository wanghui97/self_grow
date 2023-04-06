package com.wanghui.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/5 21:22
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleUpdateDto {
    //角色id
    private Long id;
    //备注信息
    private String remark;
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //菜单列表
    private List<Long> menuIds;
}
