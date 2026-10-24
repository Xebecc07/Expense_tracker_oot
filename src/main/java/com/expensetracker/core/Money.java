package com.expensetracker.core;

import java.util.Locale;

/** Utility class (private constructor, static helper): formats amounts as "Rs. 1,234". */
public final class Money {
    private Money() { }
    public static String format(double amount) {
        return "Rs. " + String.format(Locale.US, "%,d", Math.round(amount));
    }
}
