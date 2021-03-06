package com.example.demo.service;

import com.example.demo.dto.articleBriefDto;
import com.example.demo.dto.articleDto;
import com.example.demo.entity.article;
import com.example.demo.util.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-22 01:02
*@notes 文章操作接口
**/
public interface articleService {
    /**
     * 发布文章
     * @param title 文章标题
     * @param text 文章内容
     * @param file 文章配图
     * @param videoUrl 视频链接
     * @return
     */
    public Result createArticle(String title, String text, HttpServletRequest file, String videoUrl);

    /**
     * 获取当前用户的ID
     * @return
     */
    public Integer getUserId(String userName);

    /**
     * 删除文章
     * @param id 文章ID
     * @return
     */
    public Result deleteArticle(Integer id);

    /**
     * 查询文章列表
     * @return
     */
    public Result selectArticle(Integer page,Integer limit,boolean thisArticle, String artType);

    /**
     * 查询文章详细内容
     * @param id 文章ID
     * @return
     */
    public Result getArticle(Integer id);

    /**
     * 记录浏览记录
     * @param uid 用户ID
     * @param aid 文章ID
     */
    public void SetHistoricalRecord(Integer uid,Integer aid);

    /**
     * 将文章映射成文章简略
     * @param art 文章列表
     * @return
     */
    public List<articleBriefDto> articleToBrie(List<article> art);

}
