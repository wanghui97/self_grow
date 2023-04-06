package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-05 16:44:22
 */
@Repository
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
