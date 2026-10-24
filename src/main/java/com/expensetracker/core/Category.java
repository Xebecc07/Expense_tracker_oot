package com.expensetracker.core;

/** ENUM: a fixed set of spending categories. Each constant carries an "essential" flag (ENCAPSULATION inside an enum). */
public enum Category {
    FOOD(true), GROCERIES(true), RENT(true), TRANSPORT(true), UTILITIES(true), HEALTH(true), EDUCATION(true),
    SHOPPING(false), ENTERTAINMENT(false), SNACKS(false), FESTIVAL(false), OTHER(false);

    private final boolean essential;
    Category(boolean essential) { this.essential = essential; }
    /** Essentials are never counted as impulse buys. */
    public boolean isEssential() { return essential; }
}
