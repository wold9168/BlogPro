package com.hgd.http;

public enum HttpCode {
    SUCCESS(200, "成功"), FAIL(500, "系统错误"),RELOGIN(401,"重新登录"),
    USERNAME_PASSWORD_ERROR(501, "用户名或密码错误"),
    TOKENERROR(502, "请重新登录");
    private int code;
    private String msg;

    HttpCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

