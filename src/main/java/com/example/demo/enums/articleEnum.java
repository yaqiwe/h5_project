package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum articleEnum {
    ARTICLE_IS_NULL(401,"标题或文章内容为空"),
    DELETE_IS_NULL(405,"该文章不存在或没有删除该文章的权限"),
    PAGE_IS_ERROR(406,"分页错误"),
    ARTICLE_NULL(407,"查找的文章不存在"),
    ;

    private Integer code;
    private String msg;
    articleEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
