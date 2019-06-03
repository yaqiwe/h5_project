package com.example.demo.controller;

import com.example.demo.service.articleService;
import com.example.demo.util.Result;
import com.example.demo.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/*
*@author yaqiwe
*@data 2019-05-22 01:02
*@notes
**/
@RestController
@RequestMapping("/article")
public class articleController {
    @Autowired
    articleService articles;
    /*发布文章*/
    @PostMapping(value = "/crateArticle")
    public Result crateArticle(String title, String text,HttpServletRequest srcFile){
        return articles.createArticle(title,text,srcFile,null);
    }
    /*删除自己发布的文章*/
    @PostMapping(value = "/deleteArticle")
    public Result deleteArticle(Integer id){
        return articles.deleteArticle(id);
    }
    /*获取文章列表*/
    @GetMapping(value = "/selectArticle")
    public Result selectArticle(Integer page, Integer limit,
                                @RequestParam(required = false)boolean thisArticle){
        return articles.selectArticle(page,limit,thisArticle,"文章");
    }
    /*查询文章详细信息*/
    @GetMapping(value = "/getArticle")
    public Result getArticle(Integer id){
        return articles.getArticle(id);
    }

    /*查询视频列表*/
    @GetMapping(value = "/getVideo")
    public Result getVideo(Integer page, Integer limit,
                           @RequestParam(required = false)boolean thisArticle){
        return articles.selectArticle(page,limit,thisArticle,"视频");
    }
    /*发布视频*/
    @PostMapping(value = "/createVideo")
    public Result createVideo(String title, String videoUrl,HttpServletRequest srcFile){
        return articles.createArticle(title,null,srcFile,videoUrl);
    }
}
