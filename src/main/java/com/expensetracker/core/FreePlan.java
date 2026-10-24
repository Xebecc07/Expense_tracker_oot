package com.expensetracker.core;

import java.util.EnumSet;
import java.util.Set;

/** INHERITANCE: Free plan - 30 expenses/month, category + streaks only. */
public class FreePlan extends SubscriptionPlan {
    @Override public String getName() { return "Free"; }
    @Override public int getMonthlyPrice() { return 0; }
    @Override public int getMonthlyExpenseLimit() { return 30; }
    @Override public Set<Feature> features() { return EnumSet.of(Feature.CATEGORY_TRACKING, Feature.STREAKS); }
}
