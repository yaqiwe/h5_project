package com.example.demo.service.impl;

import com.example.demo.dto.articleBriefDto;
import com.example.demo.dto.articleDto;
import com.example.demo.entity.*;
import com.example.demo.enums.articleEnum;
import com.example.demo.enums.operationEnum;
import com.example.demo.enums.userEnum;
import com.example.demo.repostry.*;
import com.example.demo.service.articleService;
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
    @Autowired
    articleService articleServices;
    @Override
    public Result addLike(Integer id) {
        article art=getArticle(id);
        if (art==null)
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(),articleEnum.ARTICLE_NULL.getMsg());
        Subject subject= SecurityUtils.getSubject();
        String userName=subject.getPrincipal().toString();
        user us=users.findByUserName(userName).get(0);
        userLike like=userLikes.findByArticleIdAndUserId(art.getId(),us.getId());
        String mag="";
        if (like!=null) {
            userLikes.delete(like);
            mag="取消点赞成功";
        }
        else{
            like=new userLike();
            like.setArticleId(art.getId());
            like.setUserId(us.getId());
            userLikes.save(like);
            mag="点赞成功";
        }
        return ResultUtil.success(mag);
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
        return ResultUtil.success(commentToDot(commentR.save(com).getId()).get(0));
    }

    @Override
    public Result collections(Integer id) {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        article art=getArticle(id);
        if (art==null)
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(),articleEnum.ARTICLE_NULL.getMsg());
        collection col=collectionR.findByArticleIdAndUserId(art.getId(),userId);
        String msg="";
        if (col!=null) {
            collectionR.delete(col);
            msg="取消收藏成功";
        }
        else {
            col=new collection();
            col.setArticleId(art.getId());
            col.setUserId(userId);
            collectionR.save(col);
            msg="添加收藏成功";
        }
        return ResultUtil.success(msg);
    }

    @Override
    public Result articleOperation() {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        List<collection> list=collectionR.findByUserId(userId);
        List<article> lis=new ArrayList<>();
        for (collection col : list)
            lis.add(articles.findById(col.getArticleId()).get());
        List<articleBriefDto> dtoList=articleServices.articleToBrie(lis);
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result selectHistory() {
        Subject subject=SecurityUtils.getSubject();
        Integer userId=users.findByUserName(subject.getPrincipal().toString()).get(0).getId();//获得当前登陆的用户ID
        List<historicalRecord> list=historicals.findByUserId(userId);
        log.info("yaqiwe{}",list.size()+userId);
        List<article> lis=new ArrayList<>();
        for (historicalRecord rec : list)
            lis.add(articles.findById(rec.getArticleId()).get());
        List<articleBriefDto> dtoList=articleServices.articleToBrie(lis);
        return ResultUtil.success(dtoList);
    }

    public article getArticle(Integer id){
        article art=null;
        try {
            art=articles.findById(id).get();
        }catch (Exception e) { }
        return art;
    }


    public List<articleDto.commentDto> commentToDot(Integer id){//将评论映射成Dto
        List<comment> list = commentR.findByArticleId(id);
        List<articleDto.commentDto> comment = new ArrayList<>();
        for (comment comment1 : list) {
            articleDto.commentDto cDto = new articleDto.commentDto();
            BeanUtils.copyProperties(comment1, cDto);
            user cuser = users.findById(comment1.getUserId()).get();
            cDto.setUserName(cuser.getUserName());
            cDto.setPictureSrc(file.getIp() + cuser.getPortraitSrc());
            comment.add(cDto);
        }
        return comment;
    }
}
