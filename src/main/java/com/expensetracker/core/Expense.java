package com.expensetracker.core;

import java.time.LocalDateTime;

/**
 * ENCAPSULATION: all fields are private and exposed only through getters.
 * INHERITANCE / POLYMORPHISM: subclasses override getType().
 */
public class Expense {
    private Long id;
    private final double amount;
    private final Category category;
    private final String description;
    private final LocalDateTime time;
    private final boolean planned;

    public Expense(Long id, double amount, Category category, String description, LocalDateTime time, boolean planned) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.id = id; this.amount = amount; this.category = category;
        this.description = description; this.time = time; this.planned = planned;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getTime() { return time; }
    public boolean isPlanned() { return planned; }

    /** Overridable: lets callers treat every expense uniformly (polymorphism). */
    public String getType() { return "Regular"; }
}
