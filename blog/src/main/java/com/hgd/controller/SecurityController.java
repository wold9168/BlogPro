package com.hgd.controller;

import com.hgd.pojo.SysUser;
import com.hgd.security.SecurityServiceImpl;
import com.hgd.security.UserDetailsImpl;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @Autowired
    private SecurityServiceImpl securityService;

    @PostMapping("/login")
    public Result login(@RequestBody SysUser user){
        return securityService.login(user);
    }

    @PostMapping("/logout")
    public Result logout() {
        return securityService.logout();
    }
}
