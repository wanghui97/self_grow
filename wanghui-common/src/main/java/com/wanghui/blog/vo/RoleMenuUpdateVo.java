package com.wanghui.blog.vo;

import com.wanghui.blog.dto.MenuTreeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class RoleMenuUpdateVo {

    //角色名称
    private List<MenuTreeDto> menus;
    //角色权限字符串
    private List<Long> checkedKeys;

}
