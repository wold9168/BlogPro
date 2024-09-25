package com.hgd.service;

import com.hgd.dto.RoleDto;
import com.hgd.pojo.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2024-08-01 10:03:12
*/
public interface SysRoleService extends IService<SysRole> {

    Result roleList(int pageNum, int pageSize, String roleName, String status);

    Result changeRoleStatus(String roleId, String status);

    Result addRole(RoleDto roleDto);

    Result getRoleById(String id);

    Result roleMenuTreeselect(String id);

    Result updateRole(RoleDto roleDto);

    Result deleteRole(String id);
}
