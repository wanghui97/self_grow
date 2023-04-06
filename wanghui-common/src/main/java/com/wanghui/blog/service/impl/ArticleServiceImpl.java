package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.dto.ArticleDto;
import com.wanghui.blog.entity.Article;
import com.wanghui.blog.entity.ArticleTag;
import com.wanghui.blog.mapper.ArticleMapper;
import com.wanghui.blog.mapper.ArticleTagMapper;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.service.ArticleTagService;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.RedisCache;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.ArticleVo;
import com.wanghui.blog.vo.HotArticleVo;
import com.wanghui.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author wanghui
 * @since 2023-03-23 11:51:34
 */
@Service("articleService")
@Transactional
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public ResponseResult selectAll(Page<Article> page) {
        //先去redis缓存中获取数据
        PageVo pageVo = redisCache.getCacheObject(CodeLibraryUtil.ARTICLE_LIST);
        if(Objects.isNull(pageVo)){
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Article::getStatus, CodeLibraryUtil.ARTICLE_STATUS_NORMAL);
            Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
            //获取分页后的文章对象列表
            List<Article> records = articlePage.getRecords();
            //给Article对象的categoryName字段赋值
            records.stream()
                    .map(article ->
                            article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                    .collect(Collectors.toList());
            //将Article的对象列表复制到ArticleVo对象列表
            List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleVo.class);
            pageVo = new PageVo(articleVos,articlePage.getTotal());

            //将热门文章列表保存到redis缓存中
            redisCache.setCacheObject(CodeLibraryUtil.ARTICLE_LIST,pageVo);
            //设置某个key值的缓存过期时间
            redisCache.expire(CodeLibraryUtil.ARTICLE_LIST,5, TimeUnit.MINUTES);
        }
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult hotArticleList() {
        //先去redis缓存中获取数据
        List<HotArticleVo> articleVos = redisCache.getCacheList(CodeLibraryUtil.HOT_ARTICLE_LIST);
        //如果缓存为空，则去数据库获取
        if(articleVos.isEmpty()){
            LambdaQueryWrapper<Article> queryWrapper= new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getStatus,CodeLibraryUtil.ARTICLE_STATUS_NORMAL);
            //按照浏览量进行排序
            queryWrapper.orderByDesc(Article::getViewCount);
            List<Article> articles = articleMapper.selectList(queryWrapper);
            //bean拷贝
            articleVos = BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);
            //将热门文章列表保存到redis缓存中
            redisCache.setCacheList(CodeLibraryUtil.HOT_ARTICLE_LIST,articleVos);
            //设置某个key值的缓存过期时间
            redisCache.expire(CodeLibraryUtil.HOT_ARTICLE_LIST,5, TimeUnit.MINUTES);
        }
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult selectById(String id) {
        //根据文章id获取文章详情
        Article article = articleMapper.selectById(id);
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateViewCount(String id) {
        /*//用文章id查询文章对象
        Article article = articleMapper.selectById(id);
        //修改文章对象的浏览次数属性
        article.setViewCount(article.getViewCount()+1);
        //用修改后的新文章对象更新员对象
        int count = articleMapper.updateById(article);
        if(count!=1){
            return ResponseResult.errorResult(500,"更新失败！");
        }*/
        //更新redis缓存中的浏览次数
        redisCache.incrementCacheMapValue(CodeLibraryUtil.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticleInfo(ArticleDto articleDto) {
        //从ArticleDto对象中获取Article对象并保存
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        int insert = articleMapper.insert(article);
        if(insert!=1){
            throw new RuntimeException("文章信息保存失败！");
        }
        //获取articleVo对象中的articleTags对象
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //调用articleTagService批处理方法批量导入
        boolean saveBatch = articleTagService.saveBatch(articleTags);
        if(!saveBatch){
            throw new RuntimeException("文章标签关联信息保存失败！");
        }
        //删除redis缓存信息
        redisCache.deleteObject(CodeLibraryUtil.ARTICLE_LIST);
        redisCache.deleteObject(CodeLibraryUtil.HOT_ARTICLE_LIST);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectAllArticlePage(Integer pageNum,Integer pageSize, String title,String summary) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(StringUtils.hasText(title),Article::getTitle,title);
        articleLambdaQueryWrapper.eq(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        articlePage = articleMapper.selectPage(articlePage, articleLambdaQueryWrapper);
        PageVo articlePageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());
        return ResponseResult.okResult(articlePageVo);
    }

    @Override
    public ResponseResult selectArticleById(Long articleId) {
        //查询文章信息
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId,articleId);
        Article article = articleMapper.selectById(articleId);
        //查询文章标签信息
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,articleId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
        //获取标签id列表
        List<Long> tagIds = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        //将Article对象转换为ArticleVo对象
        ArticleDto articleDto = BeanCopyUtils.copyBean(article, ArticleDto.class);
        articleDto.setTags(tagIds);
        return ResponseResult.okResult(articleDto);
    }

    @Override
    public ResponseResult updateByArticleId(ArticleDto articleDto) {
        //将ArticleDto转化为Article对象
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        int count = articleMapper.updateById(article);
        if(count!=1){
            throw new RuntimeException("文章信息更新失败！");
        }
        //获取articleVo对象中的articleTags对象
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        //先删除原有的标签关联信息
        articleTagMapper.delete(articleTagLambdaQueryWrapper);
        //批量保存标签关联信息
        boolean saveFlag = articleTagService.saveBatch(articleTags);
        if(!saveFlag){
            throw new RuntimeException("文章标签关联信息保存失败！");
        }
        return ResponseResult.okResult();
    }
}
