package com.expensetracker.core;

import java.util.Set;

/**
 * ABSTRACTION: defines what every plan must provide; subclasses fill in the details.
 * allows() is concrete and shared by all plans (code reuse via inheritance).
 */
public abstract class SubscriptionPlan {
    public abstract String getName();
    public abstract int getMonthlyPrice();
    /** -1 means unlimited. */
    public abstract int getMonthlyExpenseLimit();
    public abstract Set<Feature> features();

    public boolean allows(Feature feature) { return features().contains(feature); }
    public boolean isUnlimited() { return getMonthlyExpenseLimit() == -1; }
}
