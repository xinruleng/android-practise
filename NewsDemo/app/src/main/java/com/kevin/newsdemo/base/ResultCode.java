package com.kevin.newsdemo.base;

/**
 * Created by kevin on 2019/07/17 10:42.
 */
public enum ResultCode {
    OK("OK"),
    ERROR("ERROR"),
    INVALID_NAME_PASSWORD("invalid username or password"),
    TOKEN_ERROR("token error"),
    ;

    ResultCode(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    private String des;
}
