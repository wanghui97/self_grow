package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-04 16:57:51
 */
@Repository
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
