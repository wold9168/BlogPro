package com.hgd.vo;

import lombok.Data;

@Data
public class RoleVo {
    private Long id;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String status;
    private String remark;
}
