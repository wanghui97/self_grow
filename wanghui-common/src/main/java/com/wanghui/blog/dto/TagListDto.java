package com.wanghui.blog.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Description:
 *     标签列表的Dto，方便生成接口文档
 * @Author 王辉
 * @Create 2023/4/4 10:11
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TagListDto {
    @TableId
    private Long id;

    //标签名
    private String name;
    //备注
    private String remark;
}
