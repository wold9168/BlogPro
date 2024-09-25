package com.hgd.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hgd.pojo.SysUser;
import com.hgd.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserName, s);
        SysUser sysUser = sysUserService.getOne(sysUserLambdaQueryWrapper);
        if(sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        return new UserDetailsImpl(sysUser);
    }
}
