package com.wanghui.blog.util;

/**
 * ClassName: CodeLibraryUtil
 * Package: com.wanghui.blog.util
 * Description:
 *     定义码值常用常量
 * @Author 王辉
 * @Create 2023/3/23 15:49
 * @Version 1.0
 */
public class CodeLibraryUtil {
    /**
     * ARTICLE 文章表
     * 文章是正常分布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0"; //已发布文章
    /**
     * ARTICLE 文章表
     * 文章是草稿状态
     */
    public static final String ARTICLE_STATUS_DRAFT = "1"; //草稿文章
    /**
     * CATEGORY 文章分类表
     * 文章分类为正常
     */
    public static final String CATEGORY_STATUS_NORMAL = "0"; //正常的分类
    /**
     * CATEGORY 文章分类表
     * 文章分类为禁用
     */
    public static final String CATEGORY_STATUS_DRAFT = "1"; //禁用的分类

    /**
     * Link 文章分类表
     * 审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
     */
    public static final String LINK_STATUS_NORMAL = "0"; //0代表审核通过

    /**
     * CATEGORY 文章分类表
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    public static final String LINK_COMMENT = "1";
}
