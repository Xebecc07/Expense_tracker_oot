package com.expensetracker.core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Impulse = non-essential AND unplanned AND (late night OR amount > 1.5x its category average).
 * find() is public so other insights can reuse it (reuse by composition).
 */
public class ImpulseDetector implements Insight {
    @Override public String getName() { return "Impulse purchase detector"; }
    @Override public Feature getRequiredFeature() { return Feature.IMPULSE_DETECTOR; }

    public List<Expense> find(User user) {
        List<Expense> recent = user.recentExpenses();
        Map<Category, Double> avg = recent.stream().collect(
                Collectors.groupingBy(Expense::getCategory, Collectors.averagingDouble(Expense::getAmount)));
        return recent.stream().filter(e -> isImpulse(e, avg.get(e.getCategory()))).toList();
    }

    private boolean isImpulse(Expense e, double categoryAverage) {
        if (e.getCategory().isEssential() || e.isPlanned()) return false;
        return isLateNight(e.getTime()) || e.getAmount() > 1.5 * categoryAverage;
    }
    private boolean isLateNight(LocalDateTime t) { return t.getHour() >= 22 || t.getHour() < 5; }

    public double total(User user) { return find(user).stream().mapToDouble(Expense::getAmount).sum(); }

    @Override public String generate(User user) {
        List<Expense> list = find(user);
        if (list.isEmpty()) return "No impulse purchases found. Nice discipline!";
        StringBuilder sb = new StringBuilder(list.size() + " impulse purchases cost " + Money.format(total(user)) + ":\n");
        list.forEach(e -> sb.append("- ").append(e.getDescription()).append(": ").append(Money.format(e.getAmount())).append("\n"));
        return sb.toString().trim();
    }
}
