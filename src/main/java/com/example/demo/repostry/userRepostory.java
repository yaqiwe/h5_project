package com.example.demo.repostry;

import com.example.demo.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*@author yaqiwe
*@data 2019-05-20 22:57
*@notes
**/
public interface userRepostory extends JpaRepository<user,Integer> {

    List<user> findByUserName(String userName);
}
