package com.example.demo.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class articleDto {

    private Integer id;
    //用户名
    private String userName;
    //文章标题
    private String title;
    //文章配图
    private String pictureSrc;
    //文章内容
    private String article;
    //文章编辑时间
    private Timestamp createTime;
    //评论
    private List<commentDto> comment;
    private Boolean is_like=false;
    @Data
    public static class commentDto{
        //评论的用户名
        private String userName;
        //评论内容
        private String comment;
        //评论用户头像
        private String pictureSrc;
        //评论时间
        private Timestamp commentTime;
    }
}
