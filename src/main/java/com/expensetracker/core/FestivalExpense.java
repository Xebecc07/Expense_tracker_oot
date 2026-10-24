package com.expensetracker.core;

import java.time.LocalDateTime;

/** INHERITANCE: a special Expense. Category is fixed to FESTIVAL and it is always planned. */
public class FestivalExpense extends Expense {
    private final String festivalName;

    public FestivalExpense(Long id, double amount, String description, LocalDateTime time, String festivalName) {
        super(id, amount, Category.FESTIVAL, description, time, true);
        this.festivalName = festivalName;
    }
    public String getFestivalName() { return festivalName; }

    /** POLYMORPHISM: overrides the parent's behaviour. */
    @Override public String getType() { return "Festival"; }
}
