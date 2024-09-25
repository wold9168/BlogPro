package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.pojo.Article;
import com.hgd.pojo.SysMenu;
import com.hgd.service.SysMenuService;
import com.hgd.mapper.SysMenuMapper;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.Result;
import com.hgd.vo.MenuVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Shinonome
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2024-08-01 10:03:12
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Override
    public Result menuList(String status, String menuName) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getStatus, status);
        if (StringUtils.hasText(menuName)) {
            sysMenuLambdaQueryWrapper.like(SysMenu::getMenuName, menuName);
        }
        sysMenuLambdaQueryWrapper.orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        List<SysMenu> list = this.list(sysMenuLambdaQueryWrapper);
        List<MenuVo> result = MyCopyBeanUtil.copyList(list, MenuVo.class);
        return Result.ok(result);
    }

    @Override
    public Result menuCreate(SysMenu sysMenu) {
        save(sysMenu);
        return Result.ok();
    }

    @Override
    public Result menuDetail(Integer id) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.select(SysMenu::getIcon, SysMenu::getId, SysMenu::getMenuName, SysMenu::getMenuType,
                SysMenu::getOrderNum, SysMenu::getParentId, SysMenu::getPath, SysMenu::getRemark, SysMenu::getStatus,
                SysMenu::getVisible, SysMenu::getPerms);
        sysMenuLambdaQueryWrapper.eq(SysMenu::getId, id);
        SysMenu resultMenu = getOne(sysMenuLambdaQueryWrapper);
        return Result.ok(resultMenu);
    }

    @Override
    public Result menuChange(SysMenu sysMenu) {
        Long id = sysMenu.getId();
        Long parentId = sysMenu.getParentId();
        if (Objects.equals(id, parentId)) return Result.fail("修改菜单'写博文'失败，上级菜单不能选择自己");
        else {
            updateById(sysMenu);
            return Result.ok();
        }
    }

    @Override
    public Result menuDelete(Integer menuId) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getParentId, menuId);
        List<SysMenu> childList = list(sysMenuLambdaQueryWrapper);
        if (!childList.isEmpty()) {
            return Result.fail("存在子菜单不允许删除");
        } else {
            removeById(menuId);
            return Result.ok();
        }
    }

    @Override
    public Result menuTreeSelect() {
//        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapperRoot = new LambdaQueryWrapper<>();
//        sysMenuLambdaQueryWrapperRoot.select(SysMenu::getId, SysMenu::getParentId, SysMenu::getMenuName);
//        List<SysMenu> list = this.list(sysMenuLambdaQueryWrapperRoot);
//        List<MenuVo> menuResult = MyCopyBeanUtil.copyList(list, MenuVo.class);
        Long isAtRootLevel = 0L;

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapperAll = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapperAll.select(SysMenu::getId, SysMenu::getParentId, SysMenu::getMenuName);
//        sysMenuLambdaQueryWrapperAll.orderByAsc(SysMenu::getParentId);
        List<SysMenu> allList = this.list(sysMenuLambdaQueryWrapperAll);
        List<MenuVo> menuTree = MyCopyBeanUtil.copyList(allList, MenuVo.class);
        menuTree.stream().forEach(menuVo -> menuVo.setChildren(new ArrayList<>()));
        menuTree.stream().forEach(menuVo -> menuVo.setLabel(menuVo.getMenuName()));
        for (MenuVo menuVo : menuTree) {
            if (Objects.equals(menuVo.getParentId(), isAtRootLevel)) {
                continue;
            } else {
                MenuVo sysMenuParent = menuTree.stream().filter(vo -> Objects.equals(vo.getId(), menuVo.getParentId())).findFirst().get();
                sysMenuParent.getChildren().add(menuVo);
            }
        }
        List<MenuVo> topLevelMenuTree = MyCopyBeanUtil.copyList(menuTree, MenuVo.class);
        topLevelMenuTree= topLevelMenuTree.stream().filter(vo -> Objects.equals(vo.getParentId(), isAtRootLevel)).collect(Collectors.toList());
        return Result.ok(topLevelMenuTree);
    }

}




