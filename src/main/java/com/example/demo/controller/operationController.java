package com.example.demo.controller;

import com.example.demo.service.operationService;
import com.example.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
*@author yaqiwe
*@data 2019-05-22 15:37
*@notes 用户操纵
**/
@RestController
@RequestMapping(value = "/operation")
public class operationController {
    @Autowired
    operationService operation;
    /*点赞*/
    @PostMapping(value = "/addLike")
    public Result addLike(Integer id){
        return operation.addLike(id);
    }

    /*评论*/
    @PostMapping(value = "/comment")
    public Result comment(Integer id,String text){
        return operation.comments(id,text);
    }
    /*收藏*/
    @PostMapping(value = "collection")
    public Result collection(Integer id){
        return operation.collections(id);
    }

    /*查询收藏列表*/
    @GetMapping(value = "articleOperation")
    public Result articleOperation(){
        return operation.articleOperation();
    }

    /*查询历史记录*/
    @GetMapping(value = "/selectHistory")
    public Result selectHistory(){
        return operation.selectHistory();
    }
}
