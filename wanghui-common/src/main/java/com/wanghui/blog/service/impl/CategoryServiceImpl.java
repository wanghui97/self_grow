package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.entity.Category;
import com.wanghui.blog.entity.Link;
import com.wanghui.blog.mapper.ArticleMapper;
import com.wanghui.blog.mapper.CategoryMapper;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.RedisCache;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.CategoryVo;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 分类表(Category)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
@Service("categoryService")
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;


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
        //将分类列表保存到redis缓存中
        redisCache.setCacheList(CodeLibraryUtil.CATEGORY_LIST,categoryVos);
        //设置某个key值的缓存过期时间
        redisCache.expire(CodeLibraryUtil.CATEGORY_LIST,5, TimeUnit.MINUTES);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus,CodeLibraryUtil.CATEGORY_STATUS_NORMAL);
        List<Category> categories = categoryMapper.selectList(categoryLambdaQueryWrapper);
        //将Category对象转换为CategoryVo对象
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, String categoryName, String status) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(StringUtils.hasText(categoryName),Category::getName,categoryName);
        categoryLambdaQueryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> categoryPage = new Page<>(pageNum,pageSize);
        categoryPage = categoryMapper.selectPage(categoryPage, categoryLambdaQueryWrapper);
        PageVo pageVo = new PageVo(categoryPage.getRecords(), categoryPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult selectCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        return ResponseResult.okResult(category);
    }
}

