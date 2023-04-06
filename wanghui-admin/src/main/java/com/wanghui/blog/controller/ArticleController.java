package com.wanghui.blog.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.dto.ArticleDto;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表控制层
 *
 * @author wanghui
 * @since 2023-03-23 11:53:15
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    /**
     * 服务对象
     */
    @Resource
    private ArticleService articleService;

    /**
     * 新增博文信息
     * @param
     * @return 所有数据
     */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName="新增博文信息")
    @ApiOperation(value = "新增博文信息",notes = "新增博文信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleVo", value = "文章信息")
    })
    public ResponseResult addArticleInfo(@RequestBody ArticleDto articleDto) {
        //AddArticleDto 对象中包含了ArticleTag信息，所以还需要存储ArticleTag信息
        return articleService.addArticleInfo(articleDto);
    }

    /**
     * 查询文章列表
     * @param pageNum: 页码
     * @param pageSize: 每页条数
     * @param title：文章标题
     * @param summary：文章摘要
     * @return 所有数据
     */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName="查询文章列表")
    @ApiOperation(value = "查询文章列表",notes = "查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(name = "title", value = "文章标题"),
            @ApiImplicitParam(name = "summary", value = "文章摘要")
    })
    public ResponseResult selectAllArticlePage(Integer pageNum,Integer pageSize, String title,String summary) {
        return articleService.selectAllArticlePage(pageNum,pageSize,title,summary);
    }

    /**
     * 查询文章详情
     * @param articleId 文章编号
     * @return 所有数据
     */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="查询文章详情")
    @ApiOperation(value = "查询文章详情",notes = "根据文章id查询文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章编号")
    })
    public ResponseResult selectArticleById(@PathVariable("id") Long articleId) {
        return articleService.selectArticleById(articleId);
    }

    /**
     * 根据文章id更新文章详情
     * @param articleDto 文章编号
     * @return 所有数据
     */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName="根据文章id更新文章详情")
    @ApiOperation(value = "根据文章id更新文章详情",notes = "根据文章id查询文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleDto", value = "文章编号")
    })
    public ResponseResult updateByArticleId(@RequestBody ArticleDto articleDto) {
        return articleService.updateByArticleId(articleDto);
    }

    /**
     * 根据文章id删除文章信息
     * @param articleId 文章编号
     * @return 所有数据
     */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="根据文章id删除文章信息")
    @ApiOperation(value = "根据文章id删除文章信息",notes = "根据文章id删除文章信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章编号")
    })
    public ResponseResult deleteArticleById(@PathVariable("id") String articleId) {
        String[] articleIdArray = articleId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> articleIds = Arrays.asList(articleIdArray).stream()
                .map(articleListId -> Long.valueOf(articleListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(articleService.removeByIds(articleIds));
    }
}

