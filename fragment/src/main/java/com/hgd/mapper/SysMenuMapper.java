package com.hgd.mapper;

import com.hgd.pojo.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgd.vo.MenuVo;

import java.util.List;

/**
* @author Shinonome
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2024-08-01 10:03:12
* @Entity com.hgd.pojo.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    public List<String> getPermsByUserId(Long userId);
    public List<MenuVo> getMenusByUserId(Long userId);
    public List<MenuVo> getMenusAll();
    public List<String> getPermsAll();
}




