package com.expensetracker.core;

import java.time.*;
import java.util.*;

/**
 * ENCAPSULATION: owns the user's data and guards it (e.g. addExpense enforces the plan limit).
 * COMPOSITION: a User HAS-A plan, expenses, goals and festivals.
 * A Clock is injected so tests can fix "today".
 */
public class User {
    private final String name;
    private final double income;
    private final double monthlyBudget;
    private SubscriptionPlan plan;
    private final List<Expense> expenses = new ArrayList<>();
    private final List<Goal> goals = new ArrayList<>();
    private final List<FestivalPlan> festivals = new ArrayList<>();
    private double emergencyFund;
    private Clock clock = Clock.systemDefaultZone();

    public User(String name, double income, double monthlyBudget, SubscriptionPlan plan) {
        this.name = name; this.income = income; this.monthlyBudget = monthlyBudget; this.plan = plan;
    }

    /** Adds an expense or throws the custom checked exception when the monthly plan limit is reached. */
    public void addExpense(Expense e) throws ExpenseLimitReachedException {
        int limit = plan.getMonthlyExpenseLimit();
        if (limit != -1) {
            YearMonth month = YearMonth.from(e.getTime());
            long used = expenses.stream().filter(x -> YearMonth.from(x.getTime()).equals(month)).count();
            if (used >= limit) throw new ExpenseLimitReachedException(plan.getName(), limit);
        }
        expenses.add(e);
    }
    /** Used when loading saved data from the database: bypasses the plan limit (it was checked when first saved). */
    public void restoreExpense(Expense e) { expenses.add(e); }
    public void addGoal(Goal g) { goals.add(g); }
    public void addFestival(FestivalPlan f) { festivals.add(f); }
    public void depositToEmergencyFund(double amount) { emergencyFund += amount; }

    public double getDailyBudget() { return monthlyBudget / 30.0; }
    public LocalDateTime now() { return LocalDateTime.now(clock); }
    public LocalDate today() { return LocalDate.now(clock); }

    /** Expenses from the last 'days' days (up to now). */
    public List<Expense> recentExpenses(int days) {
        LocalDateTime from = now().minusDays(days), to = now();
        return expenses.stream().filter(e -> e.getTime().isAfter(from) && !e.getTime().isAfter(to)).toList();
    }
    public List<Expense> recentExpenses() { return recentExpenses(30); }
    public double recentSpend() { return recentExpenses().stream().mapToDouble(Expense::getAmount).sum(); }

    public String getName() { return name; }
    public double getIncome() { return income; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public SubscriptionPlan getPlan() { return plan; }
    public void setPlan(SubscriptionPlan plan) { this.plan = plan; }
    public List<Expense> getExpenses() { return Collections.unmodifiableList(expenses); }
    public List<Goal> getGoals() { return Collections.unmodifiableList(goals); }
    public List<FestivalPlan> getFestivals() { return Collections.unmodifiableList(festivals); }
    public double getEmergencyFund() { return emergencyFund; }
    public void setClock(Clock clock) { this.clock = clock; }
}
