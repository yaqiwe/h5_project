package com.example.demo.service.impl;

import com.example.demo.dto.articleBriefDto;
import com.example.demo.dto.articleDto;
import com.example.demo.entity.*;
import com.example.demo.enums.articleEnum;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


/*
 *@author yaqiwe
 *@data 2019-05-22 01:02
 *@notes
 **/
@Slf4j
@Service
public class articleServiceImpl implements articleService {
    @Autowired
    articleRepostory articles;
    @Autowired
    userRepostory users;
    @Autowired
    commentRepostory comments;
    @Autowired
    collectionRepostory collections;
    @Autowired
    userLikeRepostory userLikes;
    @Autowired
    historicalRecordRepostory historicals;
    @Autowired
    fileSrcUtil files;
    @Autowired
    operationService operationServices;
    @Autowired
    articleTypeRepostory articleTypes;
    static Integer USER_NULL = -1;
    String urlFenGe = "&/&%%";

    @Override
    public Result createArticle(String title, String text, HttpServletRequest request, String videoUrl) {
        if (title == null || title.trim().isEmpty())
            return ResultUtil.error(articleEnum.ARTICLE_IS_NULL.getCode(), articleEnum.ARTICLE_IS_NULL.getMsg());
        Subject subject = SecurityUtils.getSubject();
        String userName = (String) subject.getPrincipal();
        Integer userId = getUserId(userName);
        if (userId == USER_NULL)
            return ResultUtil.error(userEnum.USER_NULL.getCode(), userEnum.USER_NULL.getMsg());
        article art = new article();
        art.setTitle(title);
        art.setUserId(userId);

        if(videoUrl!=null && !videoUrl.trim().isEmpty()){
            art.setArticle(videoUrl);
            art.setTypeId(getArticleType("视频"));
        }else if (text!=null && !text.trim().isEmpty()){
            String artUrl = files.witeDataToFile(text, userName);
            art.setTypeId(getArticleType("文章"));
            art.setArticle(artUrl);
        }else
            return ResultUtil.error(articleEnum.ARTICLE_IS_NULL.getCode(), articleEnum.ARTICLE_IS_NULL.getMsg());
        //读取请求体中传递封面
        try {//读取文件
            Map<String, MultipartFile> filesList = new HashMap();
            String fileUrl = "";
            int i = 0;
            MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
            String fileName = "image-";
            while (multipart.getFile(fileName + i) != null) {
                filesList.put(fileName + i, multipart.getFile(fileName + i));
                i++;
            }
            for (String fileNamea : filesList.keySet())
                fileUrl += files.uploadFile(filesList.get(fileNamea), userName) + urlFenGe;
            //将文件路径存储到数据库
            if (!fileUrl.trim().isEmpty())
                art.setPictureSrc(fileUrl.substring(0, fileUrl.length() - urlFenGe.length()));
        } catch (Exception e) { }
        articles.save(art);
        return ResultUtil.success();
    }

    public Integer getArticleType(String typeName) {
        if (typeName != null && !typeName.trim().isEmpty()) {
            articleType a = articleTypes.findByType(typeName);
            if (a != null)
                return a.getId();
        }
        return null;
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
        if (art.getPictureSrc() != null) {//删除图片列表
            String[] uri = art.getPictureSrc().split(urlFenGe);
            for (String s : uri)
                files.deleteFile(s);
        }
        if (art.getTypeId()==getArticleType("文章"))
            files.deleteFile(art.getArticle());
        List<comment> comList = comments.findByArticleId(id);
        List<collection> colList = collections.findByArticleId(id);
        List<userLike> usLike = userLikes.findByArticleId(id);
        List<historicalRecord> list = historicals.findByUserId(id);
        for (historicalRecord rec : list) //删除浏览历史
            historicals.delete(rec);
        for (collection col : colList)//删除收藏
            collections.delete(col);
        for (comment com : comList)//删除评论
            comments.delete(com);
        for (userLike uL : usLike)//删除点赞
            userLikes.delete(uL);
        articles.delete(art);
        return ResultUtil.success();
    }

    @Override
    public Result selectArticle(Integer page, Integer limit, boolean thisArticle, String artType) {
        if (page == null || page < 1 || limit == null || limit < 1)
            return ResultUtil.error(articleEnum.PAGE_IS_ERROR.getCode(), articleEnum.PAGE_IS_ERROR.getMsg());
        Page<article> art = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        if (thisArticle) {//获取所有文章或者获取对应用户的文章
            Integer userId = getUserId(null);
            if (userId == USER_NULL)
                return ResultUtil.error(userEnum.USER_NULL.getCode(), userEnum.USER_NULL.getMsg());
            art = articles.findByTypeIdAndUserId(getArticleType(artType),userId, pageable);
        } else
            art = articles.findByTypeId(getArticleType(artType), pageable);
        if (art == null)
            return ResultUtil.success();
        //将结果映射成Dto返回
        Map<String, Object> artMap = new LinkedHashMap<>();
        artMap.put("TotalPages", art.getTotalPages());
        artMap.put("TotalElements", art.getTotalElements());
        artMap.put("data", articleToBrie(art.getContent()));
        return ResultUtil.success(artMap);
    }

    public List<articleBriefDto> articleToBrie(List<article> art) {//将文章映射成文章简略返回
        List<articleBriefDto> list = new ArrayList<>();
        for (article arts : art) {
            articleBriefDto dto = new articleBriefDto();
            BeanUtils.copyProperties(arts, dto);
            dto.setComment(comments.countByArticleId(dto.getId()));
            dto.setUserLike(userLikes.countByArticleId(dto.getId()));
            user us = users.findById(arts.getUserId()).get();
            dto.setUserName(us.getUserName());
            if (arts.getPictureSrc() != null) {//返回图片列表
                String[] uri = arts.getPictureSrc().split(urlFenGe);
                List<String> list1 = new ArrayList<>();
                for (String s : uri)
                    list1.add(files.getIp() + s);
                dto.setPictureSrc(list1);
            }
            list.add(dto);
        }
        return list;
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
        List<article> list = new ArrayList<>();
        list.add(art);
        BeanUtils.copyProperties(articleToBrie(list).get(0), dto);
        //根据情况返回文章内容或者视频地址
        if (art.getTypeId()==getArticleType("文章")){
            dto.setArticle(files.readDataToFile(art.getArticle()));
        }else if(art.getTypeId()==getArticleType("视频")) {
            dto.setArticle(art.getArticle());
        }
        dto.setCommentNumber(comments.countByArticleId(art.getId()));
        if (getUserId(null) != USER_NULL) {
            if (userLikes.findByArticleIdAndUserId(art.getId(), getUserId(null)) != null)//查询当前账号是否点赞
                dto.setIs_like(true);
            if (collections.findByArticleIdAndUserId(art.getId(), getUserId(null)) != null)//查询当前是否收藏
                dto.setIsCollection(true);
            SetHistoricalRecord(getUserId(null), art.getId());
        }
        dto.setComment(operationServices.commentToDot(dto.getId()));
        return ResultUtil.success(dto);
    }

    public void SetHistoricalRecord(Integer uid, Integer aid) {
        historicalRecord record = historicals.findByUserIdAndArticleId(uid, aid);
        Integer han = historicals.countByUserId(uid);
        if (han >= 50) {//若数据条数大于50，删除最开始的数据
            Sort sort = new Sort(Sort.Direction.DESC, "hrTime");
            List<historicalRecord> list = historicals.findByUserId(uid, sort);
            historicals.delete(list.get(list.size() - 1));
        }
        if (record != null) {
            Date date = new Date();
            record.setHrTime(new Timestamp(date.getTime()));
            return;
        } else {
            record = new historicalRecord();
            record.setArticleId(aid);
            record.setUserId(uid);
            historicals.save(record);
        }
    }
}
