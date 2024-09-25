package com.hgd.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuVo {
    private String component;
    private Date createTime;
    private String icon;
    private Long id;
    private Integer isFrame;
    private String menuName;
    private String menuType;
    private Integer orderNum;
    private Long parentId;
    private String path;
    private String perms;
    private String remark;
    private String status;
    private String visible;

    private List<MenuVo> children;
    private String label;
}
