package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/*
*@author yaqiwe
*@data 2019-05-20 21:20
*@notes 文章表
**/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //用户ID
    private Integer userId;
    //文章标题
    private String title;
    //文章配图
    private String pictureSrc;
    //文章内容
    private String article;
    //文章编辑时间
    private Timestamp createTime;
}
