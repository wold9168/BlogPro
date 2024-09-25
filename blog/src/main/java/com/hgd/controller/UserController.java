package com.hgd.controller;

import com.hgd.pojo.SysUser;
import com.hgd.security.SecurityServiceImpl;
import com.hgd.service.SysUserService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private SysUserService sysUserService;
    @GetMapping("/user/userInfo")
    public Result userInfo() {
        return securityService.userInfo();
    }

    @PutMapping("/user/userInfo")
    public Result userInfo(@RequestBody SysUser user) {
        return sysUserService.userInfo(user);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody SysUser user) {
        return sysUserService.register(user);
    }
}
