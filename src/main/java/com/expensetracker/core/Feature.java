package com.expensetracker.core;

/** ENUM: one constant per app feature; plans are defined by which Features they allow. */
public enum Feature {
    CATEGORY_TRACKING("Category tracking"), IMPULSE_DETECTOR("Impulse purchase detector"),
    HIDDEN_EXPENSES("Hidden small expenses"), GOAL_IMPACT("Goal impact calculator"),
    STREAKS("Streaks"), FESTIVAL_PLANNER("Festival expense planner"),
    EMERGENCY_SAVINGS("Emergency savings"), EXPENSE_STORY("Expense story");

    private final String label;
    Feature(String label) { this.label = label; }
    public String getLabel() { return label; }
}
