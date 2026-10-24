package com.expensetracker.core;

import java.time.temporal.ChronoUnit;

/** Tells the user how much to set aside each month for each festival. */
public class FestivalPlanner implements Insight {
    @Override public String getName() { return "Festival expense planner"; }
    @Override public Feature getRequiredFeature() { return Feature.FESTIVAL_PLANNER; }

    @Override public String generate(User user) {
        if (user.getFestivals().isEmpty()) return "No festivals planned yet.";
        StringBuilder sb = new StringBuilder();
        for (FestivalPlan f : user.getFestivals()) {
            long months = Math.max(1, ChronoUnit.MONTHS.between(user.today(), f.getDate()));
            double setAside = Math.max(0, f.getBudget() - f.getSaved()) / months;
            double spent = user.getExpenses().stream()
                    .filter(e -> e instanceof FestivalExpense fe && fe.getFestivalName().equalsIgnoreCase(f.getName()))
                    .mapToDouble(Expense::getAmount).sum();
            sb.append(f.getName()).append(": ").append(months).append(" months left, set aside ")
              .append(Money.format(setAside)).append(" per month\n");
            if (spent > f.getBudget())
                sb.append("Warning: festival spend ").append(Money.format(spent)).append(" is over budget ")
                  .append(Money.format(f.getBudget())).append("\n");
        }
        return sb.toString().trim();
    }
}
