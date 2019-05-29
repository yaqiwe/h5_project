package com.example.demo.controller;

import com.example.demo.service.userService;
import com.example.demo.util.Result;
import com.example.demo.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
*@author yaqiwe
*@data 2019-05-21 10:18
*@notes
**/
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class userController {
    @Autowired
    userService user;
    /*登录*/
    @PostMapping("/logIn")
    public Result logIn(String userName, String passWord){
       return user.logIn(userName,passWord);
    }
    /*退出登录*/
    @PostMapping(value = "/logOut")
    public Result getName(){
        return user.logOut();
    }
    /*注册*/
    @PostMapping(value = "/createUser")
    public Result createUser(String userName, String passWord){
        return user.createUser(userName,passWord);
    }
    /*更新用户信息*/
    @PostMapping(value = "updateUser")
    public  Result updateUser(@RequestParam(required = false) String passWord,
                              @RequestParam(required = false)String gender,
                              @RequestParam(required = false)String nickName,
                              @RequestParam(required = false) MultipartFile src,
                              @RequestParam(required = false) String birthday){
        return user.updateUser(passWord,gender,nickName,src,birthday);

    }
    /*未登录时的提示*/
    @GetMapping(value = "/LoginUrl")
    public Result LoginUrl(){
        return ResultUtil.error(404,"未登陆无法执行该操作");
    }
    /*没有权限时的提示*/
    @GetMapping(value = "/UnauthorizedUrl")
    public Result UnauthorizedUrl(){
        return ResultUtil.error(404,"没有该操作的权限");
    }

    /*微信登陆*/
    @PostMapping(value = "/WxLogIn")
    public Result WxLogIn(String code,String file,String nickName){
        return user.WxLogIn(code,file,nickName);
    }
}
