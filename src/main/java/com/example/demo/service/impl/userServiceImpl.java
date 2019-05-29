package com.example.demo.service.impl;
import com.example.demo.dto.userDto;
import com.example.demo.entity.user;
import com.example.demo.enums.userEnum;
import com.example.demo.repostry.userRepostory;
import com.example.demo.repostry.userTypeRepostory;
import com.example.demo.service.userService;
import com.example.demo.util.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

/*
 *@author yaqiwe
 *@data 2019-05-21 10:19
 *@notes 用户操作实现类
 **/
@Service
@Slf4j
public class userServiceImpl implements userService {
    @Autowired
    userRepostory users;
    @Autowired
    fileSrcUtil file;
    @Autowired
    userTypeRepostory usetT;
    @Override
    public Result logIn(String userName, String passWord) {
        if (userName==null|| userName.isEmpty() || passWord==null || passWord.isEmpty())
            return ResultUtil.error(userEnum.USER_IS_NULL.getCode(),userEnum.USER_IS_NULL.getMsg());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
        try {
            subject.login(token);
        } catch (Exception e) {
            return ResultUtil.error(userEnum.LOGIN_IS_ERROR.getCode(), userEnum.LOGIN_IS_ERROR.getMsg());
        }
        List<user> us = users.findByUserName(userName);
        return ResultUtil.success(userToDto(us.get(0)));
    }

    @Override
    public Result logOut() {
        Subject subject = SecurityUtils.getSubject();
        String useName=subject.getPrincipal().toString();
        subject.logout();
        return ResultUtil.success("退出登录成功:"+useName);
    }

    @Override
    public Result createUser(String userName, String passWord) {
        if (userName==null|| userName.isEmpty() || passWord==null || passWord.isEmpty())
            return ResultUtil.error(userEnum.USER_IS_NULL.getCode(),userEnum.USER_IS_NULL.getMsg());
        List<user> list=users.findByUserName(userName);
        if (list.size()>0)
            return ResultUtil.error(userEnum.USER_IS_NO_NULL.getCode(),userEnum.USER_IS_NO_NULL.getMsg());
        user us=new user();
        us.setPassWord(passWord);
        us.setUserName(userName);
        us.setNickName(userName);
        us.setPortraitSrc("moren.jpg");
        users.save(us);
        return ResultUtil.success();
    }

    @Override
    public Result updateUser(String passWord, String gender, String nickName, MultipartFile src, String birthday) {
        Subject subject=SecurityUtils.getSubject();
        String userName=subject.getPrincipal().toString();
        String[] genders=new String[]{"男","女"};
        List<user> us=users.findByUserName(userName);
        if (us.size()<0)
            return ResultUtil.error(userEnum.WHERE_ERROR.getCode(),userEnum.WHERE_ERROR.getMsg());
        if (passWord!=null && !passWord.isEmpty())
            us.get(0).setPassWord(passWord);
        if (gender!=null && !gender.isEmpty()) {
            for (String s : genders) {
                if (s.equals(gender))
                    us.get(0).setGender(s);
            }
        }
        if (nickName!=null && !nickName.isEmpty())
            us.get(0).setNickName(nickName);
        if (birthday!=null && !birthday.isEmpty())
            us.get(0).setBirthday( new Timestamp(timeUtil.stringToData(birthday).getTime()));
        if (src!=null){
            String oldUrl=us.get(0).getPortraitSrc();
            String url=file.uploadFile(src,subject.getPrincipal().toString());
            if (!oldUrl.equals("/moren.jpg") && url!=null) {
                file.deleteFile(oldUrl);
            }
            us.get(0).setPortraitSrc(url);
        }
        user saUser=users.save(us.get(0));
        return ResultUtil.success(userToDto(saUser));
    }

    @Value("${appid}")
    String appid;
    @Value("${secret}")
    String secret;
    @Override
    public Result WxLogIn(String code, String file, String nickName) {
        if (code==null|| code.isEmpty())
            return ResultUtil.error(userEnum.WX_LOGIN_ERROR.getCode(),userEnum.WX_LOGIN_ERROR.getMsg());
        Map<String,String> map=new HashMap<>();
        map.put("appid",appid);
        map.put("secret",secret);
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String re=HttpClientUtil.doGet(url,map);
        if (re!=null){
            JSONObject jsonObject=JSONObject.fromObject(re);
            String userName=jsonObject.getString("openid");
            Integer user_type=usetT.findByTypeName("微信用户").getId();
            List<user> list=users.findByUserName(userName);
            user us=null;
            if (list.size()<1){
                us=new user();
                us.setNickName(nickName);
                us.setPassWord(jsonObject.getString("session_key"));
                us.setUserName(userName);
                us.setPortraitSrc(file);
                us.setUserType(user_type);
            }else{
                us=list.get(0);
                us.setPassWord(jsonObject.getString("session_key"));
            }
            users.save(us);
            return logIn(us.getUserName(),us.getPassWord());
        }
        return ResultUtil.error(userEnum.WX_LOGIN_ERRORS.getCode(),userEnum.WX_LOGIN_ERRORS.getMsg());
    }

    public userDto userToDto(user us){
        Subject subject = SecurityUtils.getSubject();
        userDto dto=new userDto();
        BeanUtils.copyProperties(us,dto);
        Integer WX_LogIn_Type=usetT.findByTypeName("微信用户").getId();
        if (us.getUserType()!=WX_LogIn_Type)
            dto.setPortraitSrc(file.getIp()+dto.getPortraitSrc());
        dto.setToken(subject.getSession().getId().toString());
        return dto;
    }
}
