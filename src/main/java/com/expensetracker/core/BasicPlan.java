package com.expensetracker.core;

import java.util.EnumSet;
import java.util.Set;

/** INHERITANCE: Basic plan - unlimited expenses, every feature. */
public class BasicPlan extends SubscriptionPlan {
    @Override public String getName() { return "Basic"; }
    @Override public int getMonthlyPrice() { return 149; }
    @Override public int getMonthlyExpenseLimit() { return -1; }
    @Override public Set<Feature> features() { return EnumSet.allOf(Feature.class); }
}
