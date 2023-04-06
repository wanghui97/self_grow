package com.wanghui.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/6 20:56
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private String status;
    private Long userId;

}
