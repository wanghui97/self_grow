package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-06 19:04:16
 */
@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
