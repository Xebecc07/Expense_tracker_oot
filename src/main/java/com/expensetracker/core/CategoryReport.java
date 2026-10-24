package com.expensetracker.core;

import java.util.*;
import java.util.stream.Collectors;

/** INTERFACE IMPLEMENTATION: spend per category, highest first. */
public class CategoryReport implements Insight {
    @Override public String getName() { return "Category report"; }
    @Override public Feature getRequiredFeature() { return Feature.CATEGORY_TRACKING; }

    @Override public String generate(User user) {
        List<Expense> recent = user.recentExpenses();
        double total = user.recentSpend();
        if (recent.isEmpty()) return "No expenses in the last 30 days.";
        Map<Category, Double> totals = recent.stream().collect(
                Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));
        StringBuilder sb = new StringBuilder();
        totals.entrySet().stream().sorted(Map.Entry.<Category, Double>comparingByValue().reversed())
              .forEach(en -> sb.append(en.getKey()).append(": ").append(Money.format(en.getValue()))
                      .append(" (").append(Math.round(en.getValue() / total * 100)).append("%)\n"));
        return sb.toString().trim();
    }
}
