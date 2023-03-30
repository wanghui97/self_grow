package com.wanghui.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: ArticleVo
 * Package: com.wanghui.blog.vo
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/24 14:45
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    //创建时间
    private Date createTime;
}
