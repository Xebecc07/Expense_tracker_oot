package com.expensetracker.core;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Counts consecutive-day streaks; the caller can filter by StreakType. */
public class StreakTracker implements Insight {
    private final ImpulseDetector impulse = new ImpulseDetector();

    @Override public String getName() { return "Streaks"; }
    @Override public Feature getRequiredFeature() { return Feature.STREAKS; }

    public int currentStreak(User user, StreakType type) {
        if (user.getExpenses().isEmpty()) return 0;
        LocalDate earliest = user.getExpenses().stream().map(e -> e.getTime().toLocalDate()).min(Comparator.naturalOrder()).get();
        Set<LocalDate> loggedDays = user.getExpenses().stream().map(e -> e.getTime().toLocalDate()).collect(Collectors.toSet());
        Set<LocalDate> impulseDays = impulse.find(user).stream().map(e -> e.getTime().toLocalDate()).collect(Collectors.toSet());
        Map<LocalDate, Double> daily = user.getExpenses().stream().collect(Collectors.groupingBy(
                e -> e.getTime().toLocalDate(), Collectors.summingDouble(Expense::getAmount)));

        Predicate<LocalDate> counts = switch (type) {                       // each type = one rule
            case LOGGING -> loggedDays::contains;
            case NO_IMPULSE -> d -> !impulseDays.contains(d);
            case UNDER_BUDGET -> d -> daily.getOrDefault(d, 0.0) <= user.getDailyBudget();
        };

        LocalDate day = user.today();
        if (type == StreakType.LOGGING && !counts.test(day)) day = day.minusDays(1);   // grace day for today
        int streak = 0;
        while (!day.isBefore(earliest) && counts.test(day)) { streak++; day = day.minusDays(1); }
        return streak;
    }

    public static String badge(int days) {
        if (days >= 30) return "Gold badge";
        if (days >= 14) return "Silver badge";
        if (days >= 7) return "Bronze badge";
        return (7 - days) + " days to Bronze";
    }

    public String generate(User user, StreakType type) {
        int days = currentStreak(user, type);
        return type.getLabel() + ": " + days + " days - " + badge(days);
    }
    @Override public String generate(User user) { return generate(user, StreakType.LOGGING); }
}
