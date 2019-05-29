package com.example.demo.repostry;

import com.example.demo.entity.collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:56
*@notes
**/
public interface collectionRepostory extends JpaRepository<collection,Integer> {

    collection findByArticleIdAndUserId(Integer aid, Integer uid);

    List<collection> findByUserId(Integer id);
    List<collection> findByArticleId(Integer id);
}
