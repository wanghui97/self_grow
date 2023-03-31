package com.wanghui.blog.controller;


import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.Comment;
import com.wanghui.blog.service.CommentService;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论表(Comment)表控制层
 *
 * @author wanghui
 * @since 2023-03-24 10:12:31
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    /**
     * 分页查询所有数据
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @param articleId 文章id
     * * @return 所有数据
     */
    @GetMapping("/commentList")
    @SelfDefinedSystemLog(BusinessName="分页查询所有评论数据")
    @ApiOperation(value = "文章评论",notes = "查询文章评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "页大小"),
            @ApiImplicitParam(name = "articleId", value = "文章id")

    })
    public ResponseResult selectAll(String pageNum, String pageSize,String articleId) {
        ResponseResult responseResult = commentService.selectAllByArtticleId(pageNum,pageSize,articleId);
        return responseResult;
    }
    /**
     * 保存评论信息
     * @param comment 评论信息对象
     * @return 所有数据
     */
    @PostMapping("")
    @SelfDefinedSystemLog(BusinessName="保存评论信息")
    @ApiOperation(value = "保存评论",notes = "保存评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "评论详情信息")
    })
    public ResponseResult saveComment(@RequestBody Comment comment){
        ResponseResult responseResult = commentService.saveComment(comment);
        return responseResult;
    }

    /**
     * 分页查询友链页面的评论数据
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 所有数据
     */
    @GetMapping("/linkCommentList")
    @SelfDefinedSystemLog(BusinessName="分页查询友链页面的评论数据")
    @ApiOperation(value = "友链评论",notes = "获取所有友链评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(CodeLibraryUtil.LINK_COMMENT,null,pageNum,pageSize);
    }
}

