package com.hgd.service;

import com.hgd.dto.UserDto;
import com.hgd.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-07-30 15:36:54
*/
public interface SysUserService extends IService<SysUser> {

    Result userInfo(SysUser user);

    Result register(SysUser user);

    Result getUserList(int pageNum, int pageSize, String userName, String phonenumber);

    Result getRoleListAllRole();

    Result addUser(UserDto userDto);

    Result deleteUser(int id);

    Result getUserDetail(int id);

    Result updateUser(UserDto userDto);
}
