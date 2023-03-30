package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 文章表(Article)表数据库访问层
 *
 * @author wanghui
 * @since 2023-03-23 11:51:29
 */
@Repository
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
