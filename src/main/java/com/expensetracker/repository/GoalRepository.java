package com.expensetracker.repository;

import com.expensetracker.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    List<GoalEntity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
