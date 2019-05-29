package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;
/*
*@author yaqiwe
*@data 2019-05-20 20:54
* 用户表
**/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //用户名
    private String userName;
    //密码
    private String passWord;
    //昵称
    private String nickName;
    //生日
    private Timestamp birthday;
    //注册时间
    private Timestamp createTime;
    //更新时间
    private Timestamp updateTime;
    //性别
    private String gender;
    //头像
    private String portraitSrc;
    //用户类型
    private Integer userType;
}
