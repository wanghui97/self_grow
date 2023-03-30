package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
@Repository
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
