package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-03 20:20:46
 */
@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleByUserId(Long userId);

}
