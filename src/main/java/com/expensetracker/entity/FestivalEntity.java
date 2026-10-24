package com.expensetracker.entity;

import jakarta.persistence.*;
import java.time.*;

/** JPA entity: maps the domain data to table 'festivals'. Plain data holder; the rules live in the core package. */
@Entity
@Table(name = "festivals")
public class FestivalEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private LocalDate festivalDate;
    private double budget;
    private double saved;

    protected FestivalEntity() { }
    public FestivalEntity(Long userId, String name, LocalDate festivalDate, double budget, double saved) {
        this.userId = userId;
        this.name = name;
        this.festivalDate = festivalDate;
        this.budget = budget;
        this.saved = saved;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getFestivalDate() { return festivalDate; }
    public void setFestivalDate(LocalDate festivalDate) { this.festivalDate = festivalDate; }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
    public double getSaved() { return saved; }
    public void setSaved(double saved) { this.saved = saved; }
}
