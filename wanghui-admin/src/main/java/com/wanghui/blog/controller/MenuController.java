package com.wanghui.blog.controller;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.Menu;
import com.wanghui.blog.service.MenuService;
import com.wanghui.blog.service.RoleService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/5 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询目录列表信息
     * @param status : 状态
     * @param menuName： 菜单名
     * @return 所有数据
     */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName="查询文章列表")
    @ApiOperation(value = "查询文章列表",notes = "查询文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "menuName", value = "菜单名")
    })
    public ResponseResult selectAllMenu(String status, String menuName) {
        return menuService.selectAllMenu(status,menuName);
    }

    /**
     * 新增目录
     * @param menu : 目录对象
     * @return 所有数据
     */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName="新增目录")
    @ApiOperation(value = "新增目录",notes = "新增目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menu", value = "目录对象")
    })
    public ResponseResult addMenuInfo(@RequestBody Menu menu) {
        boolean saveFlag = menuService.save(menu);
        if(!saveFlag){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }
    /**
     * 根据id查询目录详情
     * @param menuId 目录编号
     * @return
     */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="根据id查询目录详情")
    @ApiOperation(value = "查询目录详情",notes = "根据文章id查询文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "目录编号")
    })
    public ResponseResult selectMenuById(@PathVariable("id") Long menuId) {
        return ResponseResult.okResult(menuService.getById(menuId));
    }

    /**
     * 修改目录信息
     * @param menu : 目录对象
     * @return 所有数据
     */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName="修改目录信息")
    @ApiOperation(value = "修改目录信息",notes = "修改目录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menu", value = "目录对象")
    })
    public ResponseResult updateMenuById(@RequestBody Menu menu) {
        //如果父菜单设置为当前菜单
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"修改菜单'"+menu.getMenuName()+"'失败，上级菜单不能选择自己");
        }
        boolean updateFlag = menuService.updateById(menu);
        if(!updateFlag){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    /**
     * 根据id删除目录详情
     * @param menuId 目录编号
     * @return
     */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="根据id删除目录详情")
    @ApiOperation(value = "删除目录详情",notes = "根据id删除目录详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "目录编号")
    })
    public ResponseResult deleteMenuById(@PathVariable("id") Long menuId) {
        return ResponseResult.okResult(menuService.removeById(menuId));
    }

    /**
     * 获取菜单树图
     * @return
     */
    @GetMapping("/treeselect")
    @SelfDefinedSystemLog(BusinessName="获取菜单树图")
    @ApiOperation(value = "获取菜单树图",notes = "获取菜单树图")
    @ApiImplicitParams({
    })
    public ResponseResult menuTreeSelect() {
        return menuService.menuTreeSelect();
    }

    /**
     * 根据角色id查询角色菜单树信息
     * @param roleId 角色编号
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    @SelfDefinedSystemLog(BusinessName="根据角色id查询角色菜单树信息")
    @ApiOperation(value = "查询角色菜单树",notes = "根据角色id查询角色菜单树信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色编号")
    })
    public ResponseResult roleMenuTreeSelectByRoleId(@PathVariable("id") Long roleId) {
        return roleService.roleMenuTreeSelectByRoleId(roleId);
    }
}
