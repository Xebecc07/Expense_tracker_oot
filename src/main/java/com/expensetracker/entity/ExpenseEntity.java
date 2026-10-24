package com.expensetracker.entity;

import jakarta.persistence.*;
import java.time.*;

/** JPA entity: maps the domain data to table 'expenses'. Plain data holder; the rules live in the core package. */
@Entity
@Table(name = "expenses")
public class ExpenseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime spentAt;
    private boolean planned;
    private String festivalName;

    protected ExpenseEntity() { }
    public ExpenseEntity(Long userId, double amount, String category, String description, LocalDateTime spentAt, boolean planned, String festivalName) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.spentAt = spentAt;
        this.planned = planned;
        this.festivalName = festivalName;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getSpentAt() { return spentAt; }
    public void setSpentAt(LocalDateTime spentAt) { this.spentAt = spentAt; }
    public boolean isPlanned() { return planned; }
    public void setPlanned(boolean planned) { this.planned = planned; }
    public String getFestivalName() { return festivalName; }
    public void setFestivalName(String festivalName) { this.festivalName = festivalName; }
}
