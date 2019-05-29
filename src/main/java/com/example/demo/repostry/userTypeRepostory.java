package com.example.demo.repostry;

import com.example.demo.entity.userType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:57
*@notes
**/
public interface userTypeRepostory extends JpaRepository<userType,Integer> {
    userType findByTypeName(String name);
}
