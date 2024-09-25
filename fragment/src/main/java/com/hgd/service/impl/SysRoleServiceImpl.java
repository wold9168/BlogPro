package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.dto.RoleDto;
import com.hgd.mapper.SysRoleMenuMapper;
import com.hgd.pojo.Article;
import com.hgd.pojo.ArticleTag;
import com.hgd.pojo.SysRole;
import com.hgd.pojo.SysRoleMenu;
import com.hgd.service.SysMenuService;
import com.hgd.service.SysRoleMenuService;
import com.hgd.service.SysRoleService;
import com.hgd.mapper.SysRoleMapper;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.Result;
import com.hgd.vo.ListVo;
import com.hgd.vo.MenuVo;
import com.hgd.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Shinonome
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2024-08-01 10:03:12
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Result roleList(int pageNum, int pageSize, String roleName, String status) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.select(SysRole::getId,SysRole::getRoleKey,SysRole::getRoleName,
                SysRole::getRoleSort,SysRole::getStatus);
        if(StringUtils.hasText(roleName)){
            sysRoleLambdaQueryWrapper.like(SysRole::getRoleName, roleName);
        }
        if(StringUtils.hasText(status)){
            sysRoleLambdaQueryWrapper.eq(SysRole::getStatus, status);
        }
        Page<SysRole> sysRolePage = new Page<>(pageNum, pageSize);
        Page<SysRole> page = page(sysRolePage, sysRoleLambdaQueryWrapper);
        List<SysRole> roleList = page.getRecords();
        ListVo<SysRole> sysRoleListVo = new ListVo<>(page.getTotal(),roleList);
        return Result.ok(sysRoleListVo);
    }

    @Override
    public Result changeRoleStatus(String roleId, String status) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getId, roleId);
        SysRole sysRole = getOne(sysRoleLambdaQueryWrapper);
        sysRole.setStatus(status);
        updateById(sysRole);
        return Result.ok();
    }

    @Override
    public Result addRole(RoleDto roleDto) {
//        SysRole sysRole = new SysRole();
//        sysRole.setRoleName(roleDto.getRoleName());
//        sysRole.setRoleKey(roleDto.getRoleKey());
//        sysRole.setRoleSort(roleDto.getRoleSort());
//        sysRole.setStatus(roleDto.getStatus());
//        sysRole.setRemark(roleDto.getRemark());
        SysRole sysRole = MyCopyBeanUtil.copyBean(roleDto,SysRole.class);
        save(sysRole);
        List<Long> menuIds = roleDto.getMenuIds();
        List<SysRoleMenu> sysRoleMenuList = menuIds.stream().map(aLong -> new SysRoleMenu(sysRole.getId(),aLong)).collect(Collectors.toList());
        sysRoleMenuService.saveBatch(sysRoleMenuList);
        return Result.ok();
    }

    @Override
    public Result getRoleById(String id) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getId, id);
        SysRole sysRole = getOne(sysRoleLambdaQueryWrapper);
        RoleVo roleVo = MyCopyBeanUtil.copyBean(sysRole,RoleVo.class);
        return Result.ok(roleVo);
    }

    @Override
    public Result roleMenuTreeselect(String id) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, id);
        List<SysRoleMenu> list = sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        List<String> menuIdList = list.stream().map(sysRoleMenu -> String.valueOf(sysRoleMenu.getMenuId())).collect(Collectors.toList());
        List<MenuVo> menus = (List<MenuVo>) sysMenuService.menuTreeSelect().getData();


        Map<String,Object> map = new HashMap<String, Object>();
        map.put("menus",menus);
        map.put("checkedKeys",menuIdList);
        return Result.ok(map);
    }

    @Override
    @Transactional
    public Result updateRole(RoleDto roleDto) {
        SysRole role = MyCopyBeanUtil.copyBean(roleDto,SysRole.class);
        updateById(role);

        roleDto.getMenuIds();

        Long roleId = role.getId();
        List<Long> menuIdsList = roleDto.getMenuIds();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("role_id",roleId);
        sysRoleMenuMapper.deleteByMap(columnMap);
        List<SysRoleMenu> roleMenuList = menuIdsList.stream().map(aLong -> new SysRoleMenu(roleId,aLong)).collect(Collectors.toList());
        sysRoleMenuService.saveBatch(roleMenuList);
        return Result.ok();
    }

    @Override
    public Result deleteRole(String id) {
        sysRoleMapper.deleteById(id);
        return Result.ok();
    }
}




