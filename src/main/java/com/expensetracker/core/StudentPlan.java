package com.expensetracker.core;

import java.util.EnumSet;
import java.util.Set;

/** INHERITANCE: Student plan - needs a student ID; adds four insight features. */
public class StudentPlan extends SubscriptionPlan {
    private final String studentId;

    public StudentPlan(String studentId) {
        if (studentId == null || studentId.isBlank())
            throw new IllegalArgumentException("A student ID is required for the Student plan");
        this.studentId = studentId;
    }
    public String getStudentId() { return studentId; }
    @Override public String getName() { return "Student"; }
    @Override public int getMonthlyPrice() { return 49; }
    @Override public int getMonthlyExpenseLimit() { return 150; }
    @Override public Set<Feature> features() {
        return EnumSet.of(Feature.CATEGORY_TRACKING, Feature.STREAKS, Feature.IMPULSE_DETECTOR,
                Feature.HIDDEN_EXPENSES, Feature.GOAL_IMPACT, Feature.EMERGENCY_SAVINGS);
    }
}
