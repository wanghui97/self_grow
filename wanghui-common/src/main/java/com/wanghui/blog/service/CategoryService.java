package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.entity.Category;
import com.wanghui.blog.util.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
