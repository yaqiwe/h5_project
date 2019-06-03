package com.example.demo.dto;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Data
public class articleBriefDto {
    /*文章ID*/
    private Integer id;
    //文章标题
    private String title;
    //用户名
    private String nickName;
    //文章配图
    private List<String> pictureSrc;
    //文章编辑时间
    private Timestamp createTime;
    //评论数量
    private Integer comment;
    //点赞数量
    private Integer userLike;
    //类型文章或视频
    private Integer typeId;
}
