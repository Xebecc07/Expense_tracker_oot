package com.expensetracker.core;

import static com.expensetracker.core.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlanTest {
    @Test void freePlanAllowsOnlyCategoryAndStreaks() {
        SubscriptionPlan p = new FreePlan();
        assertTrue(p.allows(Feature.CATEGORY_TRACKING));
        assertTrue(p.allows(Feature.STREAKS));
        assertFalse(p.allows(Feature.IMPULSE_DETECTOR));
    }
    @Test void studentPlanNeedsStudentId() {
        assertThrows(IllegalArgumentException.class, () -> new StudentPlan(" "));
        SubscriptionPlan p = new StudentPlan("S123");
        assertTrue(p.allows(Feature.GOAL_IMPACT));
        assertFalse(p.allows(Feature.FESTIVAL_PLANNER));
        assertFalse(p.allows(Feature.EXPENSE_STORY));
    }
    @Test void basicPlanAllowsEverythingAndIsUnlimited() {
        SubscriptionPlan p = new BasicPlan();
        for (Feature f : Feature.values()) assertTrue(p.allows(f));
        assertTrue(p.isUnlimited());
    }
    @Test void freePlanStopsAtThirtyExpenses() throws Exception {
        User u = user(new FreePlan());
        for (int i = 0; i < 30; i++) add(u, 10, Category.FOOD, 0);
        assertThrows(ExpenseLimitReachedException.class, () -> add(u, 10, Category.FOOD, 0));
    }
    @Test void basicPlanHasNoLimit() throws Exception {
        User u = user(new BasicPlan());
        for (int i = 0; i < 200; i++) add(u, 10, Category.FOOD, 0);
        assertEquals(200, u.getExpenses().size());
    }
    @Test void lockedInsightReturnsUpgradeMessage() {
        String msg = new ImpulseDetector().run(user(new FreePlan()));
        assertTrue(msg.startsWith("Locked"));
    }
    @Test void moneyFormat() { assertEquals("Rs. 1,234", Money.format(1234)); }
}
