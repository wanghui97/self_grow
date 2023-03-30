package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.entity.Comment;
import com.wanghui.blog.util.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author wanghui
 * @since 2023-03-24 10:12:31
 */
public interface CommentService extends IService<Comment> {

    ResponseResult selectAllByArtticleId(String pageNum, String pageSize, String articleId);

    ResponseResult saveComment(Comment comment);

    ResponseResult commentList(String linkComment, Object o, Integer pageNum, Integer pageSize);
}
