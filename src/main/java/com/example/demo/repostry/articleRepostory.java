package com.example.demo.repostry;

import com.example.demo.entity.article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:56
*@notes
**/
public interface articleRepostory extends JpaRepository<article,Integer> {
    article findByIdAndUserId(Integer artId,Integer userId);

    Page<article> findByUserId(Integer id, Pageable page);

    //查询文章或者视频
    Page<article> findByTypeId(Integer type, Pageable page);
    //当前用户查询文章或者视频
    Page<article> findByTypeIdAndUserId(Integer type, Integer id, Pageable page);
}
