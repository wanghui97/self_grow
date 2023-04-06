package com.wanghui.blog.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "文章",description = "文章相关接口")
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
    @SelfDefinedSystemLog(BusinessName="分页查询所有文章信息列表")
    @ApiOperation(value = "文章列表",notes = "查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "页大小")
    })
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
    @SelfDefinedSystemLog(BusinessName="查询热门文章列表")
    @ApiOperation(value = "热门文章",notes = "查询热门文章列表")
    @ApiImplicitParams({
    })
    public ResponseResult hotArticleList() {
        ResponseResult responseResult = articleService.hotArticleList();
        return responseResult;
    }
    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @SelfDefinedSystemLog(BusinessName="查询文章详情")
    @ApiOperation(value = "文章详情",notes = "查询文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id")
    })
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
    @SelfDefinedSystemLog(BusinessName="更新文章浏览次数")
    @ApiOperation(value = "更新文章浏览次数",notes = "更新文章浏览次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id")
    })
    public ResponseResult update(@PathVariable("id") String id) {
        ResponseResult responseResult = articleService.updateViewCount(id);
        return responseResult;
    }
}

