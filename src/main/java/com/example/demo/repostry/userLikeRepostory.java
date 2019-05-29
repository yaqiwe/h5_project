package com.example.demo.repostry;

import com.example.demo.entity.userLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:57
*@notes
**/
public interface userLikeRepostory extends JpaRepository<userLike,Integer> {
    /*查询文章的点赞数量*/
    Integer countByArticleId(Integer id);

    userLike findByArticleIdAndUserId(Integer aid,Integer uid);
}
