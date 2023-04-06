package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.dto.MenuTreeDto;
import com.wanghui.blog.entity.Menu;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author wanghui
 * @since 2023-04-03 20:17:29
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult selectAllMenu(String status, String menuName);

    ResponseResult menuTreeSelect();

    List<MenuTreeDto> selectMenuTreeByMenuIds(List<Long> menuIds);
}
