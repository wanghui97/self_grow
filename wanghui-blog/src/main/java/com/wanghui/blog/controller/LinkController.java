package com.wanghui.blog.controller;


import com.wanghui.blog.service.CommentService;
import com.wanghui.blog.service.LinkService;
import com.wanghui.blog.util.ResponseResult;
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
    @Autowired
    private CommentService commentService;

    /**
     * 友联查询
     * @return 所有数据
     */
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}

