package com.expensetracker.core;

import java.time.LocalDate;

/** A savings goal (encapsulated data + one derived value). */
public class Goal {
    private final String name;
    private final double target;
    private double saved;
    private final LocalDate deadline;

    public Goal(String name, double target, double saved, LocalDate deadline) {
        this.name = name; this.target = target; this.saved = saved; this.deadline = deadline;
    }
    public String getName() { return name; }
    public double getTarget() { return target; }
    public double getSaved() { return saved; }
    public void addSaved(double amount) { this.saved += amount; }
    public LocalDate getDeadline() { return deadline; }
    public double getRemaining() { return Math.max(0, target - saved); }
}
