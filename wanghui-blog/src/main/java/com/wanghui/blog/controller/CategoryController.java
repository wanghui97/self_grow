package com.wanghui.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 分类表(Category)表控制层
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;
    /**
     * 分页查询所有数据
     * @return 所有数据
     */
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}

