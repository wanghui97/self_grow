package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author wanghui
 * @since 2023-03-24 10:12:31
 */
@Repository
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
