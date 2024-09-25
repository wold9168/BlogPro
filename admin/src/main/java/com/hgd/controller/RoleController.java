package com.hgd.controller;

import com.hgd.dto.RoleDto;
import com.hgd.pojo.SysRole;
import com.hgd.service.SysRoleService;
import com.hgd.service.SysUserRoleService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/system/role/list")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result roleList(int pageNum, int pageSize, String roleName, String status){
        return sysRoleService.roleList(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/system/role/changeStatus")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result changeRoleStatus(@RequestBody String roleId,@RequestBody String status){
        return sysRoleService.changeRoleStatus(roleId,status);
    }
    @PostMapping("/system/role")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result addRole(@RequestBody RoleDto roleDto){
        return sysRoleService.addRole(roleDto);
    }
    @GetMapping("/system/role/{id}")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result getRoleById(@PathVariable String id){
        return sysRoleService.getRoleById(id);
    }
    @GetMapping("/system/menu/roleMenuTreeselect/{id}")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result roleMenuTreeselect(@PathVariable String id){
        return sysRoleService.roleMenuTreeselect(id);
    }
    @PutMapping("/system/role")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result updateRole(@RequestBody RoleDto roleDto){
        return sysRoleService.updateRole(roleDto);
    }
    @DeleteMapping("/system/role/{id}")
    @PreAuthorize("@ps.prePost('system:role:list')")
    public Result deleteRole(@PathVariable String id){
        return sysRoleService.deleteRole(id);
    }

}
