package com.example.demo.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class articleDto {

    private Integer id;
    //用户名
    private String nickName;
    //文章标题
    private String title;
    //文章配图
    private List<String> pictureSrc;
    //文章内容
    private String article;
    //文章编辑时间
    private Timestamp createTime;
    //评论数量
    private Integer commentNumber;
    //评论
    private List<commentDto> comment;
    //是否收藏
    private Boolean isCollection=false;
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
