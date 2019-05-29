package com.example.demo.controller;

import com.example.demo.service.articleService;
import com.example.demo.util.Result;
import com.example.demo.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result crateArticle(String title, String text, MultipartFile srcFile){
        return articles.createArticle(title,text,srcFile);
    }
    /*删除自己发布的文章*/
    @PostMapping(value = "/deleteArticle")
    public Result deleteArticle(Integer id){
        return articles.deleteArticle(id);
    }
    /*获取文章列表*/
    @GetMapping(value = "/selectArticle")
    public Result selectArticle(Integer page, Integer limit,
                                @RequestParam(required = false)String userName){
        return articles.selectArticle(page,limit,userName);
    }
    /*查询文章详细信息*/
    @GetMapping(value = "/getArticle")
    public Result getArticle(Integer id){
        return articles.getArticle(id);
    }
}
