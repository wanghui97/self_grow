package com.wanghui.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.dto.TagListDto;
import com.wanghui.blog.entity.Tag;
import com.wanghui.blog.service.TagService;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *      请求处理类
 * @Author 王辉
 * @Create 2023/4/3 17:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
    * 查询分页后的标签信息列表
    * */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName = "查询分页后的标签信息列表")
    public ResponseResult<PageVo> list(Integer pageNum,Integer pageSize,TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    /**
     * 新增标签信息
     * */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName = "新增标签信息")
    public ResponseResult save(@RequestBody Tag tag){
        boolean saveFlag = tagService.save(tag);
        if(saveFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 删除标签信息
     * */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "删除标签信息")
    public ResponseResult delete(@PathVariable("id") String tagId){
        String[] tagIdArray = tagId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> tagIds = Arrays.asList(tagIdArray).stream()
                .map(tagListId -> Long.valueOf(tagListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(tagService.removeByIds(tagIds));
    }

    /**
     * 根据标签id获取标签信息
     * */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "根据标签id获取标签信息")
    public ResponseResult selectTagById(@PathVariable("id") Long tagId){
        return tagService.selectTagById(tagId);
    }

    /**
     * 修该标签信息
     * */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName = "修该标签信息")
    public ResponseResult updateTagById(@RequestBody Tag tag){
        boolean updateFlag = tagService.updateById(tag);
        if(updateFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }


    /**
     * 查询所有标签信息
     * */
    @GetMapping("/listAllTag")
    @SelfDefinedSystemLog(BusinessName = "修该标签信息")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
