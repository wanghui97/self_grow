package com.wanghui.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanghui.blog.dto.RoleMenuSaveDto;
import com.wanghui.blog.dto.RoleUpdateDto;
import com.wanghui.blog.entity.Role;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.RoleVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author wanghui
 * @since 2023-04-03 20:20:46
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleByUserId(Long id);

    ResponseResult selectAllRolePage(Integer pageNum, Integer pageSize, String status, String roleName);

    ResponseResult updateRoleStatusById(RoleVo roleVo);

    ResponseResult addRoleInfo(RoleMenuSaveDto roleMenuVO);

    ResponseResult roleMenuTreeSelectByRoleId(Long roleId);

    ResponseResult updateRoleById(RoleUpdateDto roleUpdateDto);

    ResponseResult selectAllRole();
}
