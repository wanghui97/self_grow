package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.dto.MenuTreeDto;
import com.wanghui.blog.entity.Menu;
import com.wanghui.blog.mapper.MenuMapper;
import com.wanghui.blog.mapper.UserMapper;
import com.wanghui.blog.service.MenuService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author wanghui
 * @since 2023-04-03 20:17:29
 */
@Service("menuService")
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //需求：如果用户id是1，即为超级管理员，拥有所有menu_type为C和F,且未删除的权限信息(M为目录信息)
        if(id == 1L){
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.eq(Menu::getStatus, CodeLibraryUtil.STATUS_NORMAL);
            menuLambdaQueryWrapper.in(Menu::getMenuType,CodeLibraryUtil.MENU_TYPE_F,CodeLibraryUtil.MENU_TYPE_C);
            //根据需求查询出menu列表
            List<Menu> menuList = list(menuLambdaQueryWrapper);
            //获取menu列表中每个menu对象的perms属性集合
            List<String> perms = menuList.stream()
                    .map( menu->menu.getPerms())
                    .distinct()
                    .collect(Collectors.toList());
            return perms;
        }
        //若果是其他用户，按表中配置的查询展示即可
        List<String> perms = menuMapper.selectPermsByUserId(id);
        return perms;
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menuList = null;
        //判断是否管理员
        if(userId.equals(1L)){
            //如果是，返回所有符合要求的menu
            menuList = menuMapper.selectAllRouterMenu();
        }else{
            //如果不是，返回当前用户具有的menu
            menuList = menuMapper.selectRouterMenuByUserId(userId);
        }
        //将查询出的Menu集合转换成MenuVo集合
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);
        //构建成tree的形式
        //先找出第一级菜单，然后给其children属性赋值
        List<MenuVo> menuTree = buildTree(menuVos,0L);

        //返回封装好的数据
        return menuTree;
    }

    /**
     * 根据父菜单的id构建Menu树
     * */
    private List<MenuVo> buildTree(List<MenuVo> menuVoList,Long parentId) {
        return menuVoList.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menuVoList)))
                .collect(Collectors.toList());
    }

    /**
    * 根据 menuVo 的parentId 获取他的子节点(menuList是该用户所有的菜单信息列表)
    * */
    private List<MenuVo> getChildren(MenuVo menuVo,List<MenuVo> menuVoList) {
        //获取menuvoList集合中parentId为menuVo的id的所有菜单就是menuVo的children
        return menuVoList.stream()
                .filter(menuVoFilter -> menuVoFilter.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildren(m, menuVoList)))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseResult selectAllMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        menuLambdaQueryWrapper.eq(StringUtils.hasText(menuName),Menu::getMenuName,menuName);

        List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult menuTreeSelect() {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
        //将原Menu对象转化成MenuTreeDto对象，因为MenuTreeDto的大多字段与Menu对象不一致，所以不使用工具类转化
        List<MenuTreeDto> menuTreeDtos = menus.stream()
                .map(menu -> new MenuTreeDto(menu.getId(),menu.getMenuName(),menu.getParentId()))
                .collect(Collectors.toList());
        //构建MenuTreeDto对象的树形结构
        List<MenuTreeDto> menuTreeDtoTree= buildMenuTreeDtoTree(menuTreeDtos,0L);
        return ResponseResult.okResult(menuTreeDtoTree);
    }

    private List<MenuTreeDto> buildMenuTreeDtoTree(List<MenuTreeDto> menuTreeDtos, long menuParentId) {
        //先获取所有的同一级节点
        List<MenuTreeDto> parentMenuTreeDtos = menuTreeDtos.stream()
                .filter(menuTreeDto -> menuTreeDto.getParentId().equals(menuParentId))
                .collect(Collectors.toList());
        //给所有的同一级节点的子节点赋值，返回一个完整的MenuTreeDto对象(children赋值后)
        List<MenuTreeDto> parentMenuTreeDtoComplete = parentMenuTreeDtos.stream()
                .map(parentMenuTreeDto -> parentMenuTreeDto.setChildren(getSelfChildren(parentMenuTreeDto,menuTreeDtos)))
                .collect(Collectors.toList());
        return parentMenuTreeDtoComplete;
    }

    /**
     * 从 menuTreeDtos 对象集合中获取 parentMenuTreeDto 对象的子节点
     * */
    private List<MenuTreeDto> getSelfChildren(MenuTreeDto parentMenuTreeDto, List<MenuTreeDto> menuTreeDtos) {
        //返回父节点 parentMenuTreeDto 对象的所有子节点对象
        List<MenuTreeDto> parentMenuTreeDtoChildren = menuTreeDtos.stream()
                .filter(menuTreeDto -> menuTreeDto.getParentId().equals(parentMenuTreeDto.getId()))
                .map(menuTreeDtoChildren -> menuTreeDtoChildren.setChildren(getSelfChildren(menuTreeDtoChildren,menuTreeDtos)))
                .collect(Collectors.toList());
        return parentMenuTreeDtoChildren;
    }

    @Override
    public List<MenuTreeDto> selectMenuTreeByMenuIds(List<Long> menuIds){
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getStatus,CodeLibraryUtil.STATUS_NORMAL);
        List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
        /*if(menuIds.size()>1){
            menus = menuMapper.selectBatchIds(menuIds);
        }*/
        //将原Menu对象转化成MenuTreeDto对象，因为MenuTreeDto的大多字段与Menu对象不一致，所以不使用工具类转化
        List<MenuTreeDto> menuTreeDtos = menus.stream()
                .map(menu -> new MenuTreeDto(menu.getId(),menu.getMenuName(),menu.getParentId()))
                .collect(Collectors.toList());
        //构建MenuTreeDto对象的树形结构
        List<MenuTreeDto> menuTreeDtoTree= buildMenuTreeDtoTree(menuTreeDtos,0L);
        return menuTreeDtoTree;
    }
}
