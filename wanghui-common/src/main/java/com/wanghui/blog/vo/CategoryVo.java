package com.wanghui.blog.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: CategoryVo
 * Package: com.wanghui.blog.vo
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/23 17:25
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    @TableId
    private Long id;
    //分类名
    private String name;
    //父分类id，如果没有父分类为-1
    private Long pid;
}
