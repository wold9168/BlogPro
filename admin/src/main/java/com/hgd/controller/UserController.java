package com.hgd.controller;

import com.hgd.dto.UserDto;
import com.hgd.pojo.SysUser;
import com.hgd.service.SysUserService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/system/user/list")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result getUserList(int pageNum, int pageSize, String userName,String phonenumber) {
        return sysUserService.getUserList(pageNum,pageSize,userName,phonenumber);
    }
    @GetMapping("/system/role/listAllRole")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result getRoleListAllRole() {
        return sysUserService.getRoleListAllRole();
    }
    @PostMapping("/system/user")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result addUser(@RequestBody UserDto userDto) {
        return sysUserService.addUser(userDto);
    }
    @DeleteMapping("/system/user/{id}")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result deleteUser(@PathVariable int id) {
        return sysUserService.deleteUser(id);
    }
    @GetMapping("/system/user/{id}")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result getUserDetail(@PathVariable int id) {
        return sysUserService.getUserDetail(id);
    }
    @PutMapping("/system/user")
    @PreAuthorize("@ps.prePost('system:user:list')")
    public Result updateUser(@RequestBody UserDto userDto) {
        return sysUserService.updateUser(userDto);
    }

}
