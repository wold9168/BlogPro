package com.hgd.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String userName;
    private String nickName;
    private String password;
    private String phonenumber;
    private String email;
    private String sex;
    private String status;
    private Long id;

    private List<Long> roleIds;
}
