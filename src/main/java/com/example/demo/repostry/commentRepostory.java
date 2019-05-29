package com.example.demo.repostry;

import com.example.demo.entity.comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:56
*@notes
**/
public interface commentRepostory extends JpaRepository<comment,Integer> {
    /*查询文章的评论数量*/
    Integer countByArticleId(Integer id);

    List<comment> findByArticleId(Integer id);
}
