package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum  userEnum {
    USER_IS_NULL(301,"用户名或密码为空"),
    USER_IS_NO_NULL(302,"该账号已存在"),
    LOGIN_IS_ERROR(303,"账号或密码错误"),
    FILE_IS_NULL(304,"头像文件读取错误"),
    FILE_TRANSFERTO_ERROR(305,"头像文件存储错误"),
    WHERE_ERROR(-1,"未知错误"),
    USER_NULL(305,"用户不存在"),
    WX_LOGIN_ERROR(306,"微信登陆请求参数错误"),
    WX_LOGIN_ERRORS(307,"微信登陆错误"),
    ;


    private Integer code;
    private String msg;
    userEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

}
