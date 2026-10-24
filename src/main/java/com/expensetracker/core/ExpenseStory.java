package com.expensetracker.core;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/** Plain-English summary of the last 30 days. */
public class ExpenseStory implements Insight {
    private final ImpulseDetector impulse = new ImpulseDetector();

    @Override public String getName() { return "Expense story"; }
    @Override public Feature getRequiredFeature() { return Feature.EXPENSE_STORY; }

    @Override public String generate(User user) {
        List<Expense> list = user.recentExpenses();
        if (list.isEmpty()) return "No story yet - log some expenses first.";
        double total = user.recentSpend();

        Map.Entry<Category, Double> top = list.stream().collect(
                Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get();
        Expense biggest = list.stream().max(Comparator.comparingDouble(Expense::getAmount)).get();
        Map<DayOfWeek, Double> byDay = list.stream().collect(Collectors.groupingBy(
                e -> e.getTime().getDayOfWeek(), Collectors.summingDouble(Expense::getAmount)));
        DayOfWeek priciest = Collections.max(byDay.entrySet(), Map.Entry.comparingByValue()).getKey();
        double weekend = byDay.getOrDefault(DayOfWeek.SATURDAY, 0.0) + byDay.getOrDefault(DayOfWeek.SUNDAY, 0.0);
        List<Expense> imp = impulse.find(user);

        return "This month you spent " + Money.format(total) + ", which is "
             + Math.round(total / user.getMonthlyBudget() * 100) + "% of your monthly budget.\n"
             + top.getKey() + " took the biggest share at " + Math.round(top.getValue() / total * 100) + "%.\n"
             + "Your biggest single expense was " + biggest.getDescription() + " at " + Money.format(biggest.getAmount()) + ".\n"
             + priciest.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " is your priciest weekday, and weekends make up "
             + Math.round(weekend / total * 100) + "% of spending.\n"
             + imp.size() + " impulse buys cost you " + Money.format(imp.stream().mapToDouble(Expense::getAmount).sum()) + ".";
    }
}
