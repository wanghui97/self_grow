package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.dto.TagListDto;
import com.wanghui.blog.entity.Tag;
import com.wanghui.blog.mapper.TagMapper;
import com.wanghui.blog.service.TagService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.CategoryVo;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 * * @author wanghui
 * @since 2023-04-03 16:52:55
 */
@Service("tagService")
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult pageTagList(Integer pageNum,Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagPage = new Page<>(pageNum,pageSize);
        tagPage = tagMapper.selectPage(tagPage, tagLambdaQueryWrapper);
        PageVo tagPageVo = new PageVo(tagPage.getRecords(),tagPage.getTotal());
        return ResponseResult.okResult(tagPageVo);
    }

    @Override
    public ResponseResult selectTagById(Long tagId) {
        Tag tag = tagMapper.selectById(tagId);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(tagLambdaQueryWrapper);
        return ResponseResult.okResult(tags);
    }

}
