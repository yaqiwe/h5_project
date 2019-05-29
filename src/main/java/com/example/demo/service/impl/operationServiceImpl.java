package com.example.demo.service.impl;

import com.example.demo.dto.articleBriefDto;
import com.example.demo.entity.*;
import com.example.demo.enums.articleEnum;
import com.example.demo.enums.operationEnum;
import com.example.demo.enums.userEnum;
import com.example.demo.repostry.*;
import com.example.demo.service.operationService;
import com.example.demo.util.Result;
import com.example.demo.util.ResultUtil;
import com.example.demo.util.fileSrcUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class operationServiceImpl implements operationService {
    @Autowired
    userLikeRepostory userLikes;
    @Autowired
    articleRepostory articles;
    @Autowired
    userRepostory users;
    @Autowired
    commentRepostory commentR;
    @Autowired
    collectionRepostory collectionR;
    @Autowired
    fileSrcUtil file;
    @Autowired
    historicalRecordRepostory historicals;
    @Override
    public Result addLike(Integer id) {
        article art=getArticle(id);
        if (art==null)
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(),articleEnum.ARTICLE_NULL.getMsg());
        Subject subject= SecurityUtils.getSubject();
        String userName=subject.getPrincipal().toString();
        user us=users.findByUserName(userName).get(0);
        userLike like=userLikes.findByArticleIdAndUserId(art.getId(),us.getId());
        if (like!=null)
            userLikes.delete(like);
        else{
            like=new userLike();
            like.setArticleId(art.getId());
            like.setUserId(us.getId());
            userLikes.save(like);
        }
        return ResultUtil.success();
    }

    @Override
    public Result comments(Integer id, String text) {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        if (text.isEmpty())
            return ResultUtil.error(operationEnum.COMMENT_IS_NULL.getCode(),operationEnum.COMMENT_IS_NULL.getMsg());
        article art=getArticle(id);
        if (art==null)
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(),articleEnum.ARTICLE_NULL.getMsg());
        comment com=new comment();
        com.setArticleId(art.getId());
        com.setUserId(userId);
        com.setComment(text);
        return ResultUtil.success(commentR.save(com));
    }

    @Override
    public Result collections(Integer id) {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        article art=getArticle(id);
        if (art==null)
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(),articleEnum.ARTICLE_NULL.getMsg());
        collection col=collectionR.findByArticleIdAndUserId(art.getId(),userId);
        if (col!=null)
            collectionR.delete(col);
        else {
            col=new collection();
            col.setArticleId(art.getId());
            col.setUserId(userId);
            collectionR.save(col);
        }
        return ResultUtil.success();
    }

    @Override
    public Result articleOperation() {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        List<collection> list=collectionR.findByUserId(userId);
        List<articleBriefDto> dtoList=new ArrayList<>();
        for (collection col : list) {
            articleBriefDto dto=objToDto(articles.findById(col.getArticleId()).get());
            dtoList.add(dto);
        }
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result selectHistory() {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        List<historicalRecord> list=historicals.findByUserId(userId);
        log.info("yaqiwe{}",list.size()+userId);
        List<articleBriefDto> dtoList=new ArrayList<>();
        for (historicalRecord rec : list) {
            articleBriefDto dto=objToDto(articles.findById(rec.getArticleId()).get());
            dtoList.add(dto);
        }
        return ResultUtil.success(dtoList);
    }
    //将 article映射成Dto
    public articleBriefDto objToDto(article art){
        articleBriefDto dto=new articleBriefDto();
        BeanUtils.copyProperties(art,dto);
        dto.setPictureSrc(file.getIp()+dto.getPictureSrc());
        dto.setComment(commentR.countByArticleId(dto.getId()));
        dto.setArticle(file.readDataToFile(dto.getArticle()));
        dto.setUserLike(userLikes.countByArticleId(dto.getId()));
        return dto;
    }

    public article getArticle(Integer id){
        article art=null;
        try {
            art=articles.findById(id).get();
        }catch (Exception e) { }
        return art;
    }
}
