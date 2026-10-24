package com.expensetracker.core;

/** ENUM: the kinds of streak a user can filter by. */
public enum StreakType {
    LOGGING("Logging streak"), NO_IMPULSE("No-impulse streak"), UNDER_BUDGET("Under-budget streak");
    private final String label;
    StreakType(String label) { this.label = label; }
    public String getLabel() { return label; }
}
