package com.expensetracker.core;

import java.time.LocalDate;

/** A festival budget the user is saving towards. */
public class FestivalPlan {
    private final String name;
    private final LocalDate date;
    private final double budget;
    private double saved;

    public FestivalPlan(String name, LocalDate date, double budget, double saved) {
        this.name = name; this.date = date; this.budget = budget; this.saved = saved;
    }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public double getBudget() { return budget; }
    public double getSaved() { return saved; }
    public void addSaved(double amount) { this.saved += amount; }
}
