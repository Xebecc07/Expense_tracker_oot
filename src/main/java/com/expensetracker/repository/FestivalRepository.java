package com.expensetracker.repository;

import com.expensetracker.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {
    List<FestivalEntity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
