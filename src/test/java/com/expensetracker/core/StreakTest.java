package com.expensetracker.core;

import static com.expensetracker.core.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StreakTest {
    private final StreakTracker tracker = new StreakTracker();

    @Test void loggingStreakCountsConsecutiveDays() throws Exception {
        User u = user(new FreePlan());
        for (int d = 0; d < 3; d++) add(u, 50, Category.FOOD, d);
        assertEquals(3, tracker.currentStreak(u, StreakType.LOGGING));
    }
    @Test void todayGetsGraceDayForLogging() throws Exception {
        User u = user(new FreePlan());
        add(u, 50, Category.FOOD, 1); add(u, 50, Category.FOOD, 2);
        assertEquals(2, tracker.currentStreak(u, StreakType.LOGGING));
    }
    @Test void gapBreaksLoggingStreak() throws Exception {
        User u = user(new FreePlan());
        add(u, 50, Category.FOOD, 0); add(u, 50, Category.FOOD, 1); add(u, 50, Category.FOOD, 3);
        assertEquals(2, tracker.currentStreak(u, StreakType.LOGGING));
    }
    @Test void underBudgetStopsAtOverspendDay() throws Exception {
        User u = user(new FreePlan());                       // daily budget Rs. 300
        add(u, 100, Category.FOOD, 0); add(u, 400, Category.FOOD, 1);
        assertEquals(1, tracker.currentStreak(u, StreakType.UNDER_BUDGET));
    }
    @Test void noImpulseStreakBreaksOnImpulseDay() throws Exception {
        User u = user(new StudentPlan("S1"));
        add(u, 200, Category.RENT, 0);
        add(u, 500, Category.SHOPPING, 1, 23, false);
        assertEquals(1, tracker.currentStreak(u, StreakType.NO_IMPULSE));
    }
    @Test void badges() {
        assertEquals("Bronze badge", StreakTracker.badge(7));
        assertEquals("Silver badge", StreakTracker.badge(14));
        assertEquals("Gold badge", StreakTracker.badge(30));
        assertEquals("4 days to Bronze", StreakTracker.badge(3));
    }
}
