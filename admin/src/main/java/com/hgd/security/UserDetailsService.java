package com.hgd.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hgd.mapper.SysMenuMapper;
import com.hgd.mapper.SysUserMapper;
import com.hgd.service.SysMenuService;
import com.hgd.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.hgd.pojo.SysUser;

import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = sysUserService.getOne(sysUserLambdaQueryWrapper);
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        List<String> permsByUserId = null;
        if (sysUser.getId() == 1) {
            permsByUserId = sysMenuMapper.getPermsAll();
        } else
            permsByUserId = sysMenuMapper.getPermsByUserId(sysUser.getId());
        return new UserDetailsImpl(sysUser, permsByUserId);
    }

}
