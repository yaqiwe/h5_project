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
*@data 2019-05-20 20:50
*收藏表
**/
@Data
@DynamicInsert
@DynamicUpdate
@Entity
public class collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //用户ID
    private Integer userId;
    //文章ID
    private Integer articleId;
    //收藏时间
    private Timestamp collTime;
}
