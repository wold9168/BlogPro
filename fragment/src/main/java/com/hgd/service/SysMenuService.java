package com.hgd.service;

import com.hgd.pojo.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2024-08-01 10:03:12
*/
public interface SysMenuService extends IService<SysMenu> {


    Result menuList(String status, String menuName);

    Result menuCreate(SysMenu sysMenu);

    Result menuDetail(Integer id);

    Result menuChange(SysMenu sysMenu);

    Result menuDelete(Integer menuId);

    Result menuTreeSelect();
}
