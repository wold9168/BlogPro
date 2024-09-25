package com.hgd.controller;

import com.hgd.pojo.SysUser;
import com.hgd.security.SecurityServiceImpl;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecurityController {
    @Autowired
    private SecurityServiceImpl securityService;

    @PostMapping("/user/login")
    public Result login(@RequestBody SysUser user) {
        return securityService.login(user);
    }

    @GetMapping("/getInfo")
    public Result getInfo(){
        return securityService.getInfo();
    }

    @GetMapping("/getRouters")
    public Result getRouters() {
        return securityService.getRouters();
    }

    @PostMapping("/user/logout")
    public Result logout() {
        return securityService.logout();
    }
}
