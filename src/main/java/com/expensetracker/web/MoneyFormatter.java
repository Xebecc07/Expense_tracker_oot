package com.expensetracker.web;

import com.expensetracker.core.Money;
import org.springframework.stereotype.Component;

/** Exposes the core Money helper to Thymeleaf as ${@money.format(x)}. */
@Component("money")
public class MoneyFormatter {
    public String format(double amount) { return Money.format(amount); }
}
