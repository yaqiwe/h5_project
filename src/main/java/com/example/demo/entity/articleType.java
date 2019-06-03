package com.example.demo.entity;

import lombok.Data;
import lombok.Value;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/*
*@author yaqiwe
*@data 2019-06-02 21:31
*@notes 发布类型表
**/
@Entity
@DynamicInsert
@DynamicUpdate
@Data
public class articleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    //发布的类型
    String type;
}
