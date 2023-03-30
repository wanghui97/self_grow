package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Link;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 友链(Link)表数据库访问层
 *
 * @author wanghui
 * @since 2023-03-24 16:47:44
 */
@Repository
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
