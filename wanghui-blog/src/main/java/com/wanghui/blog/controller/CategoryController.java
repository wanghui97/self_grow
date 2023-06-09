package com.wanghui.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "分类信息",description = "博客前台系统分类信息相关接口")
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
    @SelfDefinedSystemLog(BusinessName="分页查询所有分类数据")
    @ApiOperation(value = "文章分类",notes = "分页查询所有分类数据")
    @ApiImplicitParams({
    })
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}

