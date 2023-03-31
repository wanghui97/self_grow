package com.wanghui.blog.scheduled;


import com.wanghui.blog.entity.Article;
import com.wanghui.blog.service.ArticleService;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author savages
 * @date 2022/5/30 - 15:44
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/55 * * * * ?")//cron表达式,每55秒执行一次该方法
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(CodeLibraryUtil.ARTICLE_VIEW_COUNT);
        //更新到数据库中
        List<Article> articleList = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }

}
