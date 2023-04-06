package com.wanghui.blog.controller;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.dto.RoleMenuSaveDto;
import com.wanghui.blog.dto.RoleUpdateDto;
import com.wanghui.blog.entity.Role;
import com.wanghui.blog.service.RoleService;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.RoleVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/4/5 14:48
 * @Version 1.0
 */
@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询分页后的角色列表信息
     * @param pageNum: 页码
     * @param pageSize: 每页条数
     * @param roleName：角色名称
     * @param status：状态
     * @return 所有数据
     */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName="查询分页后的角色列表信息")
    @ApiOperation(value = "查询角色列表",notes = "查询分页后的角色列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(name = "roleName", value = "状态"),
            @ApiImplicitParam(name = "status", value = "角色名称")
    })
    public ResponseResult selectAllRolePage(Integer pageNum,Integer pageSize,String status, String roleName) {
        return roleService.selectAllRolePage(pageNum,pageSize,status,roleName);
    }

    /**
     * 新增角色
     * @param roleMenuSaveDto : 角色对象
     * @return
     */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName="新增角色")
    @ApiOperation(value = "新增角色",notes = "新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleMenuSaveDto", value = "角色对象")
    })
    public ResponseResult addRoleInfo(@RequestBody RoleMenuSaveDto roleMenuSaveDto) {
        return roleService.addRoleInfo(roleMenuSaveDto);
    }
    /**
     * 根据id查询角色信息详情
     * @param roleId 角色编号
     * @return
     */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="根据id查询角色信息详情")
    @ApiOperation(value = "查询角色详情",notes = "根据id查询角色信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色编号")
    })
    public ResponseResult selectRoleById(@PathVariable("id") Long roleId) {
        return ResponseResult.okResult(roleService.getById(roleId));
    }

    /**
     * 修改角色信息
     * @param roleUpdateDto : 角色对象
     * @return
     */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName="修改角色信息")
    @ApiOperation(value = "修改角色信息",notes = "根据id修改角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleUpdateDto", value = "角色对象")
    })
    public ResponseResult updateRoleById(@RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.updateRoleById(roleUpdateDto);
    }

    /**
     * 根据id删除角色详情
     * @param roleId 角色编号
     * @return
     */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName="根据id删除角色详情")
    @ApiOperation(value = "删除角色信息",notes = "根据id删除角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色编号")
    })
    public ResponseResult deleteMenuById(@PathVariable("id") String roleId) {
        String[] roloIdArray = roleId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> roleIds = Arrays.asList(roloIdArray).stream()
                .map(roleListId -> Long.valueOf(roleListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(roleService.removeByIds(roleIds));
    }

    /**
     * 修改角色状态
     * @param roleVo
     * @return
     */
    @PutMapping("/changeStatus")
    @SelfDefinedSystemLog(BusinessName="修改角色状态")
    @ApiOperation(value = "修改角色状态",notes = "修改角色状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleVo", value = "角色状态对象")
    })
    public ResponseResult updateRoleStatusById(@RequestBody RoleVo roleVo) {

        return roleService.updateRoleStatusById(roleVo);
    }

    /**
     * 查询所有角色列表信息
     * @return 所有数据
     */
    @GetMapping("/listAllRole")
    @SelfDefinedSystemLog(BusinessName="查询所有角色列表信息")
    @ApiOperation(value = "查询角色列表",notes = "查询所有角色列表信息")
    @ApiImplicitParams({
    })
    public ResponseResult selectAllRole() {
        return roleService.selectAllRole();
    }

}
