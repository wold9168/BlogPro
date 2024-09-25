package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.dto.UserDto;
import com.hgd.mapper.SysUserRoleMapper;
import com.hgd.pojo.Article;
import com.hgd.pojo.SysRole;
import com.hgd.pojo.SysUser;
import com.hgd.pojo.SysUserRole;
import com.hgd.service.SysRoleService;
import com.hgd.service.SysUserRoleService;
import com.hgd.service.SysUserService;
import com.hgd.mapper.SysUserMapper;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.Result;
import com.hgd.vo.ListVo;
import com.hgd.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Shinonome
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-07-30 15:36:54
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    private final SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public Result userInfo(SysUser user) {
        updateById(user);
        return Result.ok();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public Result register(SysUser user) {
        String userName = user.getUserName();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, userName);
        SysUser sysUser = getOne(wrapper);
        if(sysUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new RuntimeException("请填写昵称");
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new RuntimeException("请填写邮箱");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return Result.ok();
    }

    @Override
    public Result getUserList(int pageNum, int pageSize, String userName, String phonenumber) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(userName)){
            sysUserLambdaQueryWrapper.like(SysUser::getUserName, userName);
        }
        if(StringUtils.hasText(phonenumber)){
            sysUserLambdaQueryWrapper.eq(SysUser::getPhonenumber, phonenumber);
        }
        Page<SysUser> userPage = new Page<>(pageNum,pageSize);
        Page<SysUser> page = page(userPage,sysUserLambdaQueryWrapper);
        List<SysUser> list = page.getRecords();
        ListVo<SysUser> listVo = new ListVo<>(page.getTotal(),list);
        return Result.ok(listVo);
    }

    @Override
    public Result getRoleListAllRole() {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String statusNormal="0";
        sysUserLambdaQueryWrapper.eq(SysUser::getStatus,statusNormal);
        List<SysUser> list = sysUserMapper.selectList(sysUserLambdaQueryWrapper);
        return Result.ok(list);
    }

    @Override
    public Result addUser(UserDto userDto) {
        SysUser sysUser = MyCopyBeanUtil.copyBean(userDto, SysUser.class);
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        save(sysUser);
        List<Long> roleIds = userDto.getRoleIds();
        List<SysUserRole> sysUserRoleList = roleIds.stream().map(aLong -> new SysUserRole(sysUser.getId(),aLong)).collect(Collectors.toList());
        sysUserRoleService.saveBatch(sysUserRoleList);
        return Result.ok();
    }

    @Override
    public Result deleteUser(int id) {
        sysUserMapper.deleteById(id);
        return Result.ok();
    }

    @Override
    public Result getUserDetail(int id) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getId,id);
        SysUser sysUser = getOne(sysUserLambdaQueryWrapper);
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId,id);
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(sysUserRoleLambdaQueryWrapper);
        List<Long> sysUserRoleIdsList = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> sysRoleList = new ArrayList<>();
        for(Long roleId : sysUserRoleIdsList){
            LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysRoleLambdaQueryWrapper.select(SysRole::getCreateBy,SysRole::getCreateTime,
                    SysRole::getDelFlag,SysRole::getId,SysRole::getRemark,SysRole::getRoleKey,
                    SysRole::getRoleName,SysRole::getRoleSort,SysRole::getStatus,SysRole::getUpdateBy);
            sysRoleLambdaQueryWrapper.eq(SysRole::getId,roleId);
            SysRole sysRole = sysRoleService.getOne(sysRoleLambdaQueryWrapper);
            sysRoleList.add(sysRole);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("roleIds",sysUserRoleIdsList);
        map.put("roles",sysRoleList);
        UserVo userVo = MyCopyBeanUtil.copyBean(sysUser,UserVo.class);
        map.put("user",userVo);
        return Result.ok(map);
    }

    @Override
    @Transactional
    public Result updateUser(UserDto userDto) {
        SysUser sysUser = MyCopyBeanUtil.copyBean(userDto, SysUser.class);
        updateById(sysUser);

        Long userId = sysUser.getId();
        List<Long> roleIds = userDto.getRoleIds();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id",userId);
        sysUserRoleMapper.deleteByMap(columnMap);
        List<SysUserRole> sysUserRoleList = roleIds.stream().map(aLong -> new SysUserRole(userId,aLong)).collect(Collectors.toList());
        sysUserRoleService.saveBatch(sysUserRoleList);
        return Result.ok();
    }
}