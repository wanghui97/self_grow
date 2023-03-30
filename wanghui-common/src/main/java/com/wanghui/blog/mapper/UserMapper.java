package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 用户表(User)表数据库访问层
 *
 * @author wanghui
 * @since 2023-03-27 09:13:55
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
