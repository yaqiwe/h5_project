package com.example.demo.service;

import com.example.demo.util.Result;
import org.springframework.web.multipart.MultipartFile;

/*
*@author yaqiwe
*@data 2019-05-21 10:16
*@notes 用户操作
**/
public interface userService {
    /**
     * 用户登录
     * @param userName 用户名
     * @param passWord 密码
     * @return
     */
    public Result logIn(String userName,String passWord);

    /**
     * 退出登录
     * @return
     */
    public Result logOut();

    /**
     * 注册账号
     * @param userName 用户名
     * @param passWord 密码
     * @return
     */
    public Result createUser(String userName,String passWord);

    /**
     * 修改用户信息
     * @param passWord 密码
     * @param gender 性别
     * @param nickName 昵称
     * @param src 头像
     * @param birthday 生日
     * @return
     */
    public Result updateUser(String passWord, String gender, String nickName, MultipartFile src, String birthday);

    /**
     * 微信登陆
     * @param code 登陆凭证
     * @param file 头像
     * @param nickName 昵称
     * @return
     */
    public Result WxLogIn(String code,String file,String nickName);
}
