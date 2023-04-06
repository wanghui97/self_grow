package com.wanghui.blog.controller;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.dto.TagListDto;
import com.wanghui.blog.entity.Link;
import com.wanghui.blog.entity.Tag;
import com.wanghui.blog.service.LinkService;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/6 11:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 查询分页后的友链信息列表
     * */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName = "查询分页后的友链信息列表")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize,String linkName,String status){
        return linkService.pageLinkList(pageNum,pageSize,linkName,status);
    }

    /**
     * 新增友链信息
     * */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName = "新增友链信息")
    public ResponseResult save(@RequestBody Link link){
        boolean saveFlag = linkService.save(link);
        if(saveFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 删除友链信息
     * */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "删除友链信息")
    public ResponseResult delete(@PathVariable("id") String linkId){
        String[] linkIdArray = linkId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> linkIds = Arrays.asList(linkIdArray).stream()
                .map(linkListId -> Long.valueOf(linkListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(linkService.removeByIds(linkIds));
    }

    /**
     * 根据友链id获取友链信息
     * */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "根据友链id获取友链信息")
    public ResponseResult selectLinkById(@PathVariable("id") Long linkId){
        return linkService.selectLinkById(linkId);
    }

    /**
     * 修改友链信息
     * */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName = "修改友链信息")
    public ResponseResult updateLinkById(@RequestBody Link link){
        boolean updateFlag = linkService.updateById(link);
        if(updateFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 修改友链状态信息
     * */
    @PutMapping("/changeLinkStatus")
    @SelfDefinedSystemLog(BusinessName = "修改友链状态信息")
    public ResponseResult updateLinkStatusById(@RequestBody Link link){
        boolean updateFlag = linkService.updateById(link);
        if(updateFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
