package com.expensetracker.core;

/** CUSTOM CHECKED EXCEPTION: forces callers to handle "plan limit reached" (the web layer shows an upgrade prompt). */
public class ExpenseLimitReachedException extends Exception {
    private final String planName;
    private final int limit;

    public ExpenseLimitReachedException(String planName, int limit) {
        super("The " + planName + " plan allows " + limit + " expenses per month. Upgrade to add more.");
        this.planName = planName; this.limit = limit;
    }
    public String getPlanName() { return planName; }
    public int getLimit() { return limit; }
}
