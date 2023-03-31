package com.wanghui.blog.controller;


import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.service.CommentService;
import com.wanghui.blog.service.LinkService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 友链(Link)表控制层
 *
 * @author makejava
 * @since 2023-03-24 16:47:44
 */
@RestController
@RequestMapping("link")
public class LinkController{
    /**
     * 服务对象
     */
    @Autowired
    private LinkService linkService;

    /**
     * 友链查询
     * @return 所有数据
     */
    @GetMapping("/getAllLink")
    @SelfDefinedSystemLog(BusinessName="查询友链列表")
    @ApiOperation(value = "友链信息",notes = "查询友链列表")
    @ApiImplicitParams({
    })
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}

