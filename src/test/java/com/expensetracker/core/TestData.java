package com.expensetracker.core;

import java.time.*;

/** Shared helpers: a fixed clock (2026-10-24 12:00) so tests are deterministic. */
final class TestData {
    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-10-24T12:00:00Z"), ZoneOffset.UTC);
    private TestData() { }

    static User user(SubscriptionPlan plan) {
        User u = new User("Asha", 30000, 9000, plan);   // daily budget = Rs. 300
        u.setClock(CLOCK);
        return u;
    }
    static Expense add(User u, double amount, Category c, int daysAgo, int hour, boolean planned) throws Exception {
        Expense e = new Expense(null, amount, c, c.name().toLowerCase() + " item",
                LocalDateTime.of(u.today().minusDays(daysAgo), LocalTime.of(hour, 0)), planned);
        u.addExpense(e);
        return e;
    }
    static Expense add(User u, double amount, Category c, int daysAgo) throws Exception {
        return add(u, amount, c, daysAgo, 10, false);
    }
}
