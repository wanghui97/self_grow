package com.wanghui.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/5 15:43
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MenuTreeDto {
    private List<MenuTreeDto> children;
    private Long id;
    private String label;
    private Long parentId;

    public MenuTreeDto(Long id, String label, Long parentId) {
        this.id = id;
        this.label = label;
        this.parentId = parentId;
    }
}
