package com.wanghui.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: PageVo
 * Package: com.wanghui.blog.vo
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/24 8:58
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {
    private List rows;
    private long total;
}
