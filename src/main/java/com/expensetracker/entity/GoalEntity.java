package com.expensetracker.entity;

import jakarta.persistence.*;
import java.time.*;

/** JPA entity: maps the domain data to table 'goals'. Plain data holder; the rules live in the core package. */
@Entity
@Table(name = "goals")
public class GoalEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private double target;
    private double saved;
    private LocalDate deadline;

    protected GoalEntity() { }
    public GoalEntity(Long userId, String name, double target, double saved, LocalDate deadline) {
        this.userId = userId;
        this.name = name;
        this.target = target;
        this.saved = saved;
        this.deadline = deadline;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getTarget() { return target; }
    public void setTarget(double target) { this.target = target; }
    public double getSaved() { return saved; }
    public void setSaved(double saved) { this.saved = saved; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
