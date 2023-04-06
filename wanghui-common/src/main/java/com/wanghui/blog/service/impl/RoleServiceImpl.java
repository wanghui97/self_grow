package com.wanghui.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.dto.MenuTreeDto;
import com.wanghui.blog.dto.RoleMenuSaveDto;
import com.wanghui.blog.dto.RoleUpdateDto;
import com.wanghui.blog.entity.Role;
import com.wanghui.blog.entity.RoleMenu;
import com.wanghui.blog.mapper.MenuMapper;
import com.wanghui.blog.mapper.RoleMapper;
import com.wanghui.blog.mapper.RoleMenuMapper;
import com.wanghui.blog.service.MenuService;
import com.wanghui.blog.service.RoleMenuService;
import com.wanghui.blog.service.RoleService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.CodeLibraryUtil;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.PageVo;
import com.wanghui.blog.vo.RoleMenuUpdateVo;
import com.wanghui.blog.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author wanghui
 * @since 2023-04-03 20:20:46
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuService menuService;

    @Override
    public List<String> selectRoleByUserId(Long id) {
        //如果是超级管理员，返回的集合中只需要有admin
        if(id == 1L){
           List<String> roleKeys = new ArrayList<>();
           roleKeys.add("admin");
           return roleKeys;
        }
        //否则如果是普通用户
        List<String> roleKeys = roleMapper.selectRoleByUserId(id);
        return roleKeys;
    }

    @Override
    public ResponseResult selectAllRolePage(Integer pageNum, Integer pageSize, String status, String roleName) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        roleLambdaQueryWrapper.eq(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<>(pageNum,pageSize);
        rolePage = roleMapper.selectPage(rolePage, roleLambdaQueryWrapper);

        PageVo pageVo = new PageVo(rolePage.getRecords(), rolePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRoleStatusById(RoleVo roleVo) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,roleVo.getRoleId());
        //跟据roleId查询角色对象
        Role role = roleMapper.selectOne(roleLambdaQueryWrapper);
        role.setStatus(roleVo.getStatus());
        int count = roleMapper.update(role, roleLambdaQueryWrapper);
        if(count!=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRoleInfo(RoleMenuSaveDto roleMenuVO) {
        Role role = BeanCopyUtils.copyBean(roleMenuVO, Role.class);
        int saveCount = roleMapper.insert(role);
        if(saveCount!=1){
            throw new RuntimeException("保存失败！");
        }
        List<Long> menuIds = roleMenuVO.getMenuIds();
        List<RoleMenu> RoleMenus = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        boolean saveFlag = roleMenuService.saveBatch(RoleMenus);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult roleMenuTreeSelectByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuLambdaQueryWrapper);
        //获取当前角色的所关联的菜单权限id列表
        List<Long> menuIds = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        //根据菜单id集合，获取当前角色的菜单树图
        List<MenuTreeDto> menuTreeDtoList = menuService.selectMenuTreeByMenuIds(menuIds);
        //将当前角色的菜单树图和其所关联的菜单权限id列表封装到RoleMenuUpdateVo对象中
        RoleMenuUpdateVo roleMenuUpdateVo = new RoleMenuUpdateVo(menuTreeDtoList,menuIds);
        //返回封装后的对象
        return ResponseResult.okResult(roleMenuUpdateVo);
    }

    @Override
    public ResponseResult updateRoleById(RoleUpdateDto roleUpdateDto) {
        Role role = BeanCopyUtils.copyBean(roleUpdateDto, Role.class);
        int update = roleMapper.updateById(role);
        if(update!=1){
            throw new RuntimeException("角色信息更新失败！");
        }
        //删除原有的角色菜单关联信息
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,role.getId());
        boolean remove = roleMenuService.remove(roleMenuLambdaQueryWrapper);

        //保存更新的角色菜单关联信息
        List<RoleMenu> roleMenuList = roleUpdateDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(),menuId))
                .collect(Collectors.toList());
        boolean saveFlag = roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectAllRole() {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, CodeLibraryUtil.STATUS_NORMAL);
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
        return ResponseResult.okResult(roles);
    }
}
