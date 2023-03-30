package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.entity.Category;
import com.wanghui.blog.mapper.ArticleMapper;
import com.wanghui.blog.mapper.CategoryMapper;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 分类表(Category)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, CodeLibraryUtil.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(o -> o.getCategoryId())
                .collect(Collectors.toSet());
        //调用IService的批量查询接口查询分类表中数据
        List<Category> categoryList = listByIds(categoryIds);
        categoryList = categoryList.stream()
                .filter(category ->
                        category.getStatus().equals(CodeLibraryUtil.CATEGORY_STATUS_NORMAL))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}

