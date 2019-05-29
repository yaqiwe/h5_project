package com.example.demo.dto;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
public class articleBriefDto {
    /*文章ID*/
    private Integer id;
    //文章标题
    private String title;
    //文章配图
    private String pictureSrc;
    //文章内容
    private String article;
    //评论数量
    private Integer comment;
    //点赞数量
    private Integer userLike;
}
