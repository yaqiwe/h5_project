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
*@data 2019-05-20 21:39
*@notes 评论表
**/
@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //文章ID
    private Integer articleId;
    //用户ID
    private Integer userId;
    //评论内容
    private String comment;
    //评论时间
    private Timestamp commentTime;
}
