package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
*@author yaqiwe
*@data 2019-05-20 21:36
*@notes 点赞表
**/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class userLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //文章ID
    private Integer articleId;
    //用户ID
    private Integer userId;
}
