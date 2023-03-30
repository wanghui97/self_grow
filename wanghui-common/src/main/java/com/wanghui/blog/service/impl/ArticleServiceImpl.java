package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.mapper.ArticleMapper;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.ArticleVo;
import com.wanghui.blog.vo.HotArticleVo;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-23 11:51:34
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult selectAll(Page<Article> page) {

        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, CodeLibraryUtil.ARTICLE_STATUS_NORMAL);
        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
        //获取分页后的文章对象列表
        List<Article> records = articlePage.getRecords();
        //给Article对象的categoryName字段赋值
        records.stream()
                .map(article ->
                        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //将Article的对象列表复制到ArticleVo对象列表
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleVo.class);
        PageVo pageVo = new PageVo(articleVos,articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,CodeLibraryUtil.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        //bean拷贝
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult selectById(String id) {
        //根据文章id获取文章详情
        Article article = articleMapper.selectById(id);
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateViewCount(String id) {
        //用文章id查询文章对象
        Article article = articleMapper.selectById(id);
        //修改文章对象的浏览次数属性
        article.setViewCount(article.getViewCount()+1);
        //用修改后的新文章对象更新员对象
        int count = articleMapper.updateById(article);
        if(count!=1){
            return ResponseResult.errorResult(500,"更新失败！");
        }
        return ResponseResult.okResult();
    }

}
