package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum operationEnum {
    COMMENT_IS_NULL(501,"评论内容不可为空"),
    ;


    private Integer code;
    private String msg;
    operationEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
