package com.expensetracker.core;

import java.time.temporal.ChronoUnit;
import java.util.*;

/** Shows whether savings are enough for each goal and how cutting impulse + hidden spends would help. */
public class GoalImpactCalculator implements Insight {
    private final ImpulseDetector impulse = new ImpulseDetector();      // composition: reuse other insights
    private final HiddenExpenseFinder hidden = new HiddenExpenseFinder();

    @Override public String getName() { return "Goal impact calculator"; }
    @Override public Feature getRequiredFeature() { return Feature.GOAL_IMPACT; }

    @Override public String generate(User user) {
        if (user.getGoals().isEmpty()) return "No goals yet. Add one to see its impact.";
        double savings = user.getIncome() - user.recentSpend();
        // Cuttable = impulse + hidden spends (an expense in both lists is counted once).
        Set<Expense> cut = Collections.newSetFromMap(new IdentityHashMap<>());
        cut.addAll(impulse.find(user));
        cut.addAll(hidden.flaggedExpenses(user));
        double cuttable = cut.stream().mapToDouble(Expense::getAmount).sum();

        StringBuilder sb = new StringBuilder();
        for (Goal g : user.getGoals()) {
            long months = Math.max(1, ChronoUnit.MONTHS.between(user.today(), g.getDeadline()));
            double needed = g.getRemaining() / months;
            double shortfall = Math.max(0, needed - savings);
            sb.append(g.getName()).append(" (").append(months).append(" months left)\n");
            sb.append("Needed per month: ").append(Money.format(needed)).append("\n");
            sb.append("Current monthly savings: ").append(Money.format(savings)).append("\n");
            sb.append("Shortfall: ").append(Money.format(shortfall)).append("\n");
            if (shortfall > 0) {
                sb.append("Cutting impulse + hidden spends frees ").append(Money.format(cuttable))
                  .append(", covering ").append(Money.format(Math.min(shortfall, cuttable))).append(" of the shortfall\n");
                double newSavings = savings + cuttable;
                sb.append(newSavings > 0
                        ? "New estimated time to goal: " + (long) Math.ceil(g.getRemaining() / newSavings) + " months\n"
                        : "Goal not reachable at this spending level\n");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }
}
