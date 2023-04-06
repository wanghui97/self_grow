package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.dto.TagListDto;
import com.wanghui.blog.entity.Tag;
import com.wanghui.blog.util.ResponseResult;


/**
 * 标签(Tag)表服务接口
 *
 * @author wanghui
 * @since 2023-04-03 16:52:55
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum,Integer pageSize, TagListDto tagListDto);

    ResponseResult selectTagById(Long tagId);

    ResponseResult listAllTag();
}
