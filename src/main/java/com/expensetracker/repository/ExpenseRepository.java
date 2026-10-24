package com.expensetracker.repository;

import com.expensetracker.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
