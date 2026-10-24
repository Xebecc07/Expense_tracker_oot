package com.expensetracker.core;

import static com.expensetracker.core.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class InsightTest {
    private final User student() { return user(new StudentPlan("S1")); }

    @Test void categoryReportListsBiggestFirst() throws Exception {
        User u = user(new FreePlan());
        add(u, 100, Category.SHOPPING, 1); add(u, 300, Category.FOOD, 1);
        String r = new CategoryReport().run(u);
        assertTrue(r.indexOf("FOOD") < r.indexOf("SHOPPING"));
    }
    @Test void lateNightUnplannedNonEssentialIsImpulse() throws Exception {
        User u = student();
        add(u, 500, Category.SHOPPING, 1, 23, false);
        assertEquals(1, new ImpulseDetector().find(u).size());
    }
    @Test void plannedOrEssentialIsNeverImpulse() throws Exception {
        User u = student();
        add(u, 500, Category.SHOPPING, 1, 23, true);
        add(u, 5000, Category.RENT, 1, 23, false);
        assertTrue(new ImpulseDetector().find(u).isEmpty());
    }
    @Test void amountAboveOnePointFiveTimesCategoryAverageIsImpulse() throws Exception {
        User u = student();
        for (int i = 0; i < 3; i++) add(u, 100, Category.SHOPPING, 2);
        add(u, 1000, Category.SHOPPING, 2);                 // avg = 325, 1000 > 487.5
        assertEquals(1, new ImpulseDetector().find(u).size());
    }
    @Test void threeSmallSpendsAreFlaggedWithYearlyProjection() throws Exception {
        User u = student();
        for (int i = 0; i < 3; i++) add(u, 20, Category.SNACKS, i);
        HiddenExpenseFinder f = new HiddenExpenseFinder();
        assertEquals(60, f.monthlyTotal(u));
        assertTrue(f.run(u).contains("Rs. 720"));
    }
    @Test void twoSmallSpendsAreNotFlagged() throws Exception {
        User u = student();
        for (int i = 0; i < 2; i++) add(u, 20, Category.SNACKS, i);
        assertTrue(new HiddenExpenseFinder().flaggedGroups(u).isEmpty());
    }
    @Test void goalImpactShowsNeededAndShortfall() throws Exception {
        User u = student();
        add(u, 10000, Category.RENT, 1);                    // savings = 30000 - 10000 = 20000
        u.addGoal(new Goal("Laptop", 120000, 0, u.today().plusMonths(4)));   // needs 30000/month
        String r = new GoalImpactCalculator().run(u);
        assertTrue(r.contains("Needed per month: Rs. 30,000"));
        assertTrue(r.contains("Shortfall: Rs. 10,000"));
    }
    @Test void emergencySavingsUsesSixTimesEssentialSpend() throws Exception {
        User u = student();
        add(u, 5000, Category.RENT, 1); add(u, 1000, Category.GROCERIES, 1);   // essentials = 6000
        u.depositToEmergencyFund(9000);
        String r = new EmergencySavings().run(u);
        assertTrue(r.contains("Target: Rs. 36,000"));
        assertTrue(r.contains("25% of target"));
        assertTrue(r.contains("Rs. 2,250 per month"));
    }
    @Test void festivalPlannerComputesMonthlySetAside() {
        User u = user(new BasicPlan());
        u.addFestival(new FestivalPlan("Diwali", u.today().plusMonths(5), 10000, 2500));
        assertTrue(new FestivalPlanner().run(u).contains("Rs. 1,500 per month"));
    }
    @Test void festivalPlannerWarnsWhenOverBudget() throws Exception {
        User u = user(new BasicPlan());
        u.addFestival(new FestivalPlan("Diwali", u.today().plusMonths(5), 10000, 0));
        u.addExpense(new FestivalExpense(null, 12000, "Gifts", LocalDateTime.of(u.today(), java.time.LocalTime.of(9, 0)), "Diwali"));
        assertTrue(new FestivalPlanner().run(u).contains("over budget"));
    }
    @Test void polymorphicTypeOfFestivalExpense() {
        Expense e = new FestivalExpense(null, 10, "x", LocalDateTime.now(), "Holi");
        assertEquals("Festival", e.getType());
        assertEquals(Category.FESTIVAL, e.getCategory());
        assertTrue(e.isPlanned());
    }
    @Test void expenseStoryMentionsBudgetPercentAndBiggest() throws Exception {
        User u = user(new BasicPlan());
        add(u, 5000, Category.RENT, 1); add(u, 300, Category.FOOD, 1);
        String r = new ExpenseStory().run(u);
        assertTrue(r.contains("59% of your monthly budget"));
        assertTrue(r.contains("biggest single expense"));
    }
}
