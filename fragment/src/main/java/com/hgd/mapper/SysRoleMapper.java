package com.hgd.mapper;

import com.hgd.pojo.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Shinonome
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2024-08-01 10:03:12
* @Entity com.hgd.pojo.SysRole
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {
    public List<String> getRoleKeysByUserId(Long userId);

}




