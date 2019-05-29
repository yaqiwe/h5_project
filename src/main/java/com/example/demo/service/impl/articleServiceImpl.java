package com.example.demo.service.impl;

import com.example.demo.dto.articleBriefDto;
import com.example.demo.dto.articleDto;
import com.example.demo.entity.article;
import com.example.demo.entity.comment;
import com.example.demo.entity.historicalRecord;
import com.example.demo.entity.user;
import com.example.demo.enums.articleEnum;
import com.example.demo.enums.userEnum;
import com.example.demo.repostry.*;
import com.example.demo.service.articleService;
import com.example.demo.util.Result;
import com.example.demo.util.ResultUtil;
import com.example.demo.util.fileSrcUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;


/*
 *@author yaqiwe
 *@data 2019-05-22 01:02
 *@notes
 **/
@Service
public class articleServiceImpl implements articleService {
    @Autowired
    articleRepostory articles;
    @Autowired
    userRepostory users;
    @Autowired
    commentRepostory comments;
    @Autowired
    userLikeRepostory userLikes;
    @Autowired
    historicalRecordRepostory historicals;
    @Autowired
    fileSrcUtil files;
    static Integer USER_NULL = -1;

    @Override
    public Result createArticle(String title, String text, MultipartFile file) {
        if (title == null || title.isEmpty() || text == null || text.isEmpty())
            return ResultUtil.error(articleEnum.ARTICLE_IS_NULL.getCode(), articleEnum.ARTICLE_IS_NULL.getMsg());
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        Integer userId = getUserId(userName);
        if (userId == USER_NULL)
            return ResultUtil.error(userEnum.USER_NULL.getCode(), userEnum.USER_NULL.getMsg());
        article art = new article();
        art.setTitle(title);
        art.setUserId(userId);
        String artUrl = files.witeDataToFile(text, userName);
        art.setArticle(artUrl);
        if (file != null) {
            String fileUrl = files.uploadFile(file, userName);
            art.setPictureSrc(fileUrl);
        }
        articles.save(art);
        return ResultUtil.success();
    }

    @Override
    public Integer getUserId(String userName) {
        if (userName == null || userName.isEmpty()) {//如果没传入用户名则直接获取当前登陆的用户ID
            Subject subject = SecurityUtils.getSubject();
            userName = (String) subject.getPrincipal();
        }
        List<user> us = users.findByUserName(userName);
        if (us.size() > 0)
            return us.get(0).getId();
        else
            return USER_NULL;
    }

    @Override
    public Result deleteArticle(Integer id) {
        article art = articles.findByIdAndUserId(id, getUserId(null));
        if (art == null)
            return ResultUtil.error(articleEnum.DELETE_IS_NULL.getCode(), articleEnum.DELETE_IS_NULL.getMsg());
        files.deleteFile(art.getPictureSrc());
        files.deleteFile(art.getArticle());
        articles.delete(art);
        return ResultUtil.success();
    }

    @Override
    public Result selectArticle(Integer page, Integer limit, String userName) {
        if (page == null || page < 1 || limit == null || limit < 1)
            return ResultUtil.error(articleEnum.PAGE_IS_ERROR.getCode(), articleEnum.PAGE_IS_ERROR.getMsg());
        Page<article> art = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        if (userName != null && !userName.isEmpty()) {//获取所有文章或者获取对应用户的文章
            Integer userId = getUserId(userName);
            if (userId == USER_NULL)
                return ResultUtil.error(userEnum.USER_NULL.getCode(), userEnum.USER_NULL.getMsg());
            art = articles.findByUserId(userId, pageable);
        } else
            art = articles.findAll(pageable);

        if (art == null)
            return ResultUtil.success();
        //将结果映射成Dto返回
        Map<String, Object> artMap = new LinkedHashMap<>();
        artMap.put("TotalPages", art.getTotalPages());
        artMap.put("TotalElements", art.getTotalElements());
        List<articleBriefDto> list = new ArrayList<>();
        for (article arts : art.getContent()) {
            articleBriefDto dto = new articleBriefDto();
            BeanUtils.copyProperties(arts, dto);
            dto.setComment(comments.countByArticleId(dto.getId()));
            dto.setUserLike(userLikes.countByArticleId(dto.getId()));
            dto.setArticle(files.readDataToFile(dto.getArticle()));
            if (dto.getPictureSrc()!=null)
                dto.setPictureSrc(files.getIp()+dto.getPictureSrc());
            list.add(dto);
        }
        artMap.put("data", list);
        return ResultUtil.success(artMap);
    }

    @Override
    public Result getArticle(Integer id) {
        article art = null;
        try {
            art = articles.findById(id).get();
        } catch (Exception e) {
            return ResultUtil.error(articleEnum.ARTICLE_NULL.getCode(), articleEnum.ARTICLE_NULL.getMsg());
        }
        articleDto dto = new articleDto();
        BeanUtils.copyProperties(art, dto);
        user us = users.findById(art.getUserId()).get();
        dto.setUserName(us.getUserName());
        dto.setArticle(files.readDataToFile(dto.getArticle()));
        if (dto.getPictureSrc()!=null)
            dto.setPictureSrc(files.getIp()+dto.getPictureSrc());
        if (getUserId(null) != USER_NULL) {
            if (userLikes.findByArticleIdAndUserId(art.getId(), getUserId(null)) != null)//查询当前账号是否点赞
                dto.setIs_like(true);
            SetHistoricalRecord(getUserId(null),art.getId());
        }
        List<comment> list = comments.findByArticleId(dto.getId());
        List<articleDto.commentDto> comment = new ArrayList<>();
        for (comment comment1 : list) {//将评论映射成Dto
            articleDto.commentDto cDto = new articleDto.commentDto();
            BeanUtils.copyProperties(comment1, cDto);
            user cuser = users.findById(comment1.getUserId()).get();
            cDto.setUserName(cuser.getUserName());
            cDto.setPictureSrc(files.getIp() + cuser.getPortraitSrc());
            comment.add(cDto);
        }
        dto.setComment(comment);
        return ResultUtil.success(dto);
    }

    public void SetHistoricalRecord(Integer uid,Integer aid){
        historicalRecord record=historicals.findByUserIdAndArticleId(uid,aid);
        Integer han=historicals.countByUserId(uid);
        if (han>=50){//若数据条数大于50，删除最开始的数据
            Sort sort = new Sort(Sort.Direction.DESC, "hrTime");
            List<historicalRecord> list=historicals.findByUserId(uid,sort);
            historicals.delete(list.get(list.size()-1));
        }
        if (record!=null){
            Date date=new Date();
            record.setHrTime(new Timestamp(date.getTime()));
            return;
        }else {
            record = new historicalRecord();
            record.setArticleId(aid);
            record.setUserId(uid);
            historicals.save(record);
        }
    }
}
