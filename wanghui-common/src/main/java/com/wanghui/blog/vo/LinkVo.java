package com.wanghui.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: LinkVo
 * Package: com.wanghui.blog.vo
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/24 17:01
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkVo {

    private Long id;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;
}
