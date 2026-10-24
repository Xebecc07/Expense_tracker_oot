package com.expensetracker.service;

import com.expensetracker.core.Category;
import com.expensetracker.entity.*;
import com.expensetracker.repository.*;
import java.time.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Demo data so every feature shows something immediately (switches the user to the Basic plan). */
@Service
public class SeedService {
    private final UserRepository users;
    private final ExpenseRepository expenses;
    private final GoalRepository goals;
    private final FestivalRepository festivals;

    public SeedService(UserRepository users, ExpenseRepository expenses, GoalRepository goals, FestivalRepository festivals) {
        this.users = users; this.expenses = expenses; this.goals = goals; this.festivals = festivals;
    }

    @Transactional
    public void seed(Long uid) {
        UserEntity u = users.findById(uid).orElseThrow();
        u.setPlanName("BASIC"); u.setIncome(30000); u.setMonthlyBudget(18000); u.setEmergencyFund(3000);
        users.save(u);
        expenses.deleteByUserId(uid); goals.deleteByUserId(uid); festivals.deleteByUserId(uid);

        for (int d = 0; d < 12; d++) add(uid, d, 13, 110, Category.FOOD, "Canteen lunch", true);   // 12-day logging streak
        for (int d : new int[]{1, 2, 3, 4, 6}) add(uid, d, 17, 20, Category.SNACKS, "Chai", false); // hidden small spends
        for (int d : new int[]{1, 3, 5}) add(uid, d, 9, 60, Category.TRANSPORT, "Auto ride", false);
        add(uid, 20, 10, 5000, Category.RENT, "Room rent", true);
        add(uid, 12, 18, 850, Category.GROCERIES, "Groceries", true);
        add(uid, 5, 18, 640, Category.GROCERIES, "Groceries", true);
        add(uid, 18, 11, 399, Category.UTILITIES, "Mobile recharge", true);
        add(uid, 14, 15, 500, Category.EDUCATION, "Course notes", true);
        add(uid, 8, 12, 250, Category.HEALTH, "Pharmacy", false);
        add(uid, 2, 23, 1800, Category.SHOPPING, "Late-night sale shoes", false);   // impulse (night)
        add(uid, 4, 1, 399, Category.ENTERTAINMENT, "Game skin", false);            // impulse (night)
        add(uid, 9, 15, 2400, Category.SHOPPING, "Earbuds", false);                 // impulse (big amount)
        add(uid, 7, 14, 150, Category.SHOPPING, "Phone cover", false);
        add(uid, 15, 16, 600, Category.SHOPPING, "T-shirt", true);
        add(uid, 6, 20, 300, Category.ENTERTAINMENT, "Movie", false);
        expenses.save(new ExpenseEntity(uid, 1500, "FESTIVAL", "Diwali lights", at(3, 19), true, "Diwali"));

        LocalDate today = LocalDate.now();
        goals.save(new GoalEntity(uid, "New laptop", 60000, 5000, today.plusMonths(4)));
        festivals.save(new FestivalEntity(uid, "Diwali", today.plusMonths(2), 8000, 2000));
    }

    private LocalDateTime at(int daysAgo, int hour) {
        return daysAgo == 0 ? LocalDateTime.now().minusMinutes(1)
                            : LocalDateTime.of(LocalDate.now().minusDays(daysAgo), LocalTime.of(hour, 0));
    }
    private void add(Long uid, int daysAgo, int hour, double amount, Category c, String desc, boolean planned) {
        expenses.save(new ExpenseEntity(uid, amount, c.name(), desc, at(daysAgo, hour), planned, null));
    }
}
