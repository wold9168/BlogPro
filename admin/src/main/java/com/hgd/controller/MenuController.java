package com.hgd.controller;

import com.hgd.pojo.SysMenu;
import com.hgd.service.SysMenuService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class MenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @GetMapping("/system/menu/list")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuList(String status,String menuName) {
        return sysMenuService.menuList(status,menuName);
    }
    @PostMapping("/system/menu")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuCreate(@RequestBody SysMenu sysMenu){
        return sysMenuService.menuCreate(sysMenu);
    }
    @GetMapping("/system/menu/{id}")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuDetail(@PathVariable Integer id) {
        return sysMenuService.menuDetail(id);
    }
    @PutMapping("/system/menu")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuChange(@RequestBody SysMenu sysMenu){
        return sysMenuService.menuChange(sysMenu);
    }
    @DeleteMapping("/system/menu/{menuId}")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuDelete(@PathVariable Integer menuId){
        return sysMenuService.menuDelete(menuId);
    }
    @GetMapping("/system/menu/treeselect")
    @PreAuthorize("@ps.prePost('system:menu:list')")
    public Result menuTreeSelect(){
        return sysMenuService.menuTreeSelect();
    }
}
