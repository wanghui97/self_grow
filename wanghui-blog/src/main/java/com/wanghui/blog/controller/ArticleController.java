package com.wanghui.blog.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 文章表(Article)表控制层
 *
 * @author wanghui
 * @since 2023-03-23 11:53:15
 */
@RestController
@RequestMapping("article")
public class ArticleController{
    /**
     * 服务对象
     */
    @Resource
    private ArticleService articleService;

    /**
     * 分页查询所有数据
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 所有数据
     */
    @GetMapping("/articleList")
    //http://localhost:7777/article/articleList?pageNum=1&pageSize=10&categoryId=0'
    public ResponseResult selectAll(long pageNum,long pageSize) {
        Page<Article> page = new Page(pageNum,pageSize);
        ResponseResult responseResult = articleService.selectAll(page);
        return responseResult;
    }
    /**
     * 查询热门文章
     * @return 所有数据
     */
    @GetMapping("/hotArticleList")
    //http://localhost:7777/article/hotArticleList
    public ResponseResult hotArticleList() {
        ResponseResult responseResult = articleService.hotArticleList();
        return responseResult;
    }
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseResult selectOne(@PathVariable("id") String id) {
        ResponseResult responseResult = articleService.selectById(id);
        return responseResult;
    }
    /**
     * 更新文章浏览次数
     * @param id 文章id
     * @return 修改结果
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult update(@PathVariable("id") String id) {
        ResponseResult responseResult = articleService.updateViewCount(id);
        return responseResult;
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     *//*
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.articleService.removeByIds(idList));
    }*/
}

