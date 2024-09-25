package com.hgd.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hgd.http.AppInfo;
import com.hgd.http.HttpCode;
import com.hgd.mapper.SysMenuMapper;
import com.hgd.mapper.SysRoleMapper;
import com.hgd.mapper.SysUserMapper;
import com.hgd.pojo.SysMenu;
import com.hgd.pojo.SysUser;
import com.hgd.service.SysMenuService;
import com.hgd.util.*;
import com.hgd.vo.GetInfoVo;
import com.hgd.vo.MenuVo;
import com.hgd.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    public Result login(SysUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
            String token = JwtUtil.getToken(String.valueOf(userDetails.getSysUser().getId()));
            redisUtil.hSet(AppInfo.ADMIN_REDIS_KEY, String.valueOf(userDetails.getSysUser().getId()), userDetails);
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            return Result.ok(map);
        }
        return Result.fail(HttpCode.USERNAME_PASSWORD_ERROR);
    }

    @Autowired
    private SysMenuService sysMenuService;

    public Result getInfo() {
        UserDetailsImpl userDetails = SecurityUtil.getAuthUser();
        if (userDetails.getSysUser().getId() == 1) {
            LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysMenuLambdaQueryWrapper.select(SysMenu::getPerms);
            sysMenuLambdaQueryWrapper.isNotNull(SysMenu::getPerms);
            List<SysMenu> list = sysMenuService.list(sysMenuLambdaQueryWrapper);
            List<String> permsList = list.stream().map(SysMenu::getPerms).collect(Collectors.toList());
            GetInfoVo getInfoVo = new GetInfoVo();
            getInfoVo.setPermissions(permsList);
            getInfoVo.setRoles(Arrays.asList("admin"));
            getInfoVo.setUser(MyCopyBeanUtil.copyBean(userDetails.getSysUser(), UserVo.class));
            return Result.ok(getInfoVo);
        }
        List<String> permsList = sysMenuMapper.getPermsByUserId(userDetails.getSysUser().getId());
        GetInfoVo getInfoVo = new GetInfoVo();
        getInfoVo.setPermissions(permsList);
        List<String> roleKeysByUserId=
                sysRoleMapper.getRoleKeysByUserId(userDetails.getSysUser().getId());
        getInfoVo.setRoles(roleKeysByUserId);
        getInfoVo.setUser(MyCopyBeanUtil.copyBean(userDetails.getSysUser(), UserVo.class));
        return Result.ok(getInfoVo);
    }

    public Result getRouters(){
        UserDetailsImpl userDetails = SecurityUtil.getAuthUser();
        if(userDetails.getSysUser().getId() == 1){
            List<MenuVo> menuVoList = sysMenuMapper.getMenusAll();
            List<MenuVo> parentList = menuVoList.stream().filter(menuVo -> menuVo.getParentId() == 0).collect(Collectors.toList());
            for(MenuVo menuVo : parentList){
                menuVo.setChildren(getResultMenuVoList(menuVoList,menuVo.getId()));
            }
            Map<String,Object> map = new HashMap<>();
            map.put("menus",parentList);
            return Result.ok(map);
        }
        return null;
    }

    public List<MenuVo> getResultMenuVoList(List<MenuVo> menuVoList,long parentId){
        return menuVoList.stream().filter(menuVo -> menuVo.getParentId() == parentId).collect(Collectors.toList());
    }

    public Result logout() {
        try
        {
            UserDetailsImpl authUser = SecurityUtil.getAuthUser();
            redisUtil.hRemove(AppInfo.ADMIN_REDIS_KEY,
                    authUser.getSysUser().getId());
        }catch(Exception e){

        }
        return Result.ok();
    }
}
