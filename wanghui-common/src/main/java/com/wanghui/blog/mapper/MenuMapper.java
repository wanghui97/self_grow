package com.wanghui.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanghui.blog.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author wanghui
 * @since 2023-04-03 20:17:29
 */
@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuByUserId(Long userId);
}
