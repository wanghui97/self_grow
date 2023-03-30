package com.wanghui.blog.controller;


import com.wanghui.blog.entity.Comment;
import com.wanghui.blog.service.CommentService;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
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
     *
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @param articleId 文章id
     * * @return 所有数据
     */
    //http://localhost:7777/comment/commentList?pageNum=1&pageSize=10&articleId=1
    @GetMapping("/commentList")
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
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(CodeLibraryUtil.LINK_COMMENT,null,pageNum,pageSize);
    }
}

