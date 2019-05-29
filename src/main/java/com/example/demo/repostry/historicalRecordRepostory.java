package com.example.demo.repostry;

import com.example.demo.entity.historicalRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface historicalRecordRepostory extends JpaRepository<historicalRecord,Integer> {

    Integer countByUserId(Integer id);
    List<historicalRecord> findByUserId(Integer id);
    historicalRecord findByUserIdAndArticleId(Integer uid,Integer aid);
    List<historicalRecord> findByUserId(Integer id, Sort sort);
}
