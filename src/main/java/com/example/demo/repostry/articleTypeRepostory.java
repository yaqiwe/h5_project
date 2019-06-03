package com.example.demo.repostry;

import com.example.demo.entity.articleType;
import org.springframework.data.jpa.repository.JpaRepository;

/*
*@author yaqiwe
*@data 2019-06-02 21:32
*@notes
**/
public interface articleTypeRepostory  extends JpaRepository<articleType,Integer> {
    articleType findByType(String typeNaem);
}
