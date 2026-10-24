package com.expensetracker.entity;

import jakarta.persistence.*;

/** JPA entity: maps the domain data to table 'app_users'. Plain data holder; the rules live in the core package. */
@Entity
@Table(name = "app_users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pinHash;
    private double income = 30000;
    private double monthlyBudget = 18000;
    private String planName = "FREE";
    private String studentId;
    private double emergencyFund = 0;

    protected UserEntity() { }
    public UserEntity(String name, String pinHash) { this.name = name; this.pinHash = pinHash; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }
    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(double monthlyBudget) { this.monthlyBudget = monthlyBudget; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public double getEmergencyFund() { return emergencyFund; }
    public void setEmergencyFund(double emergencyFund) { this.emergencyFund = emergencyFund; }
}
