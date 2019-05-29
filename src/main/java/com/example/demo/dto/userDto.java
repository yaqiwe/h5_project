package com.example.demo.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class userDto {
    //昵称
    private String nickName;
    //生日
    private Timestamp birthday;
    //注册时间
    private Timestamp createTime;
    //性别
    private String gender;
    //头像
    private String portraitSrc;
    //token
    private String token;
}
