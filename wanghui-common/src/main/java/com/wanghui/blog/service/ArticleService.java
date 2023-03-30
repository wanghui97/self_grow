package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.util.ResponseResult;


/**
 * 文章表(Article)表服务接口
 *
 * @author wanghui
 * @since 2023-03-23 11:51:31
 */
public interface ArticleService extends IService<Article> {

    ResponseResult selectAll(Page<Article> page);

    ResponseResult hotArticleList();

    ResponseResult selectById(String id);

    ResponseResult updateViewCount(String id);
}
