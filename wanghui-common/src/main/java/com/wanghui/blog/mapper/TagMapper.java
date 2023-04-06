package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-03 16:52:50
 */
@Repository
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
