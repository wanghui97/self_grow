package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.entity.RoleMenu;
import com.wanghui.blog.mapper.RoleMenuMapper;
import com.wanghui.blog.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author wanghui
 * @since 2023-04-05 16:44:22
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
