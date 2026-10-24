package com.expensetracker.core;

import java.util.*;
import java.util.stream.Collectors;

/** Small spends (<= Rs. 100) that add up: flagged when a category has 3 or more of them. */
public class HiddenExpenseFinder implements Insight {
    public static final double SMALL_LIMIT = 100;
    public static final int MIN_COUNT = 3;

    @Override public String getName() { return "Hidden small expenses"; }
    @Override public Feature getRequiredFeature() { return Feature.HIDDEN_EXPENSES; }

    public Map<Category, List<Expense>> flaggedGroups(User user) {
        Map<Category, List<Expense>> groups = user.recentExpenses().stream()
                .filter(e -> e.getAmount() <= SMALL_LIMIT)
                .collect(Collectors.groupingBy(Expense::getCategory, () -> new EnumMap<>(Category.class), Collectors.toList()));
        groups.values().removeIf(list -> list.size() < MIN_COUNT);
        return groups;
    }
    public List<Expense> flaggedExpenses(User user) {
        return flaggedGroups(user).values().stream().flatMap(List::stream).toList();
    }
    public double monthlyTotal(User user) {
        return flaggedExpenses(user).stream().mapToDouble(Expense::getAmount).sum();
    }

    @Override public String generate(User user) {
        Map<Category, List<Expense>> groups = flaggedGroups(user);
        if (groups.isEmpty()) return "No hidden small expenses found.";
        StringBuilder sb = new StringBuilder();
        groups.forEach((c, l) -> sb.append(c).append(": ").append(l.size()).append(" small spends = ")
                .append(Money.format(l.stream().mapToDouble(Expense::getAmount).sum())).append("\n"));
        double monthly = monthlyTotal(user);
        sb.append("Monthly total: ").append(Money.format(monthly)).append("\n");
        sb.append("Yearly projection: ").append(Money.format(monthly * 12));
        return sb.toString();
    }
}
