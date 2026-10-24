package com.expensetracker.core;

/** Emergency fund target = 6 x monthly essential spend (fallback: 60% of total spend). */
public class EmergencySavings implements Insight {
    @Override public String getName() { return "Emergency savings"; }
    @Override public Feature getRequiredFeature() { return Feature.EMERGENCY_SAVINGS; }

    @Override public String generate(User user) {
        double essential = user.recentExpenses().stream().filter(e -> e.getCategory().isEssential())
                .mapToDouble(Expense::getAmount).sum();
        double monthly = essential > 0 ? essential : 0.6 * user.recentSpend();
        if (monthly <= 0) return "Add some expenses so we can size your emergency fund.";
        double target = 6 * monthly, fund = user.getEmergencyFund();
        double perMonth = Math.max(0, target - fund) / 12;
        return "Target: " + Money.format(target) + "\n"
             + "Saved: " + Money.format(fund) + " (" + Math.round(fund / target * 100) + "% of target)\n"
             + "Cover: " + String.format("%.1f", fund / monthly) + " months\n"
             + "Save " + Money.format(perMonth) + " per month to reach the target in 12 months";
    }
}
