package com.expensetracker.service;

import com.expensetracker.core.*;
import com.expensetracker.entity.*;
import com.expensetracker.repository.*;
import java.time.*;
import org.springframework.stereotype.Service;

/**
 * Service layer: loads database rows, builds the pure-Java core User, and lets the core classes
 * enforce the rules (e.g. User.addExpense throws ExpenseLimitReachedException).
 */
@Service
public class TrackerService {
    private final UserRepository users;
    private final ExpenseRepository expenses;
    private final GoalRepository goals;
    private final FestivalRepository festivals;

    public TrackerService(UserRepository users, ExpenseRepository expenses, GoalRepository goals, FestivalRepository festivals) {
        this.users = users; this.expenses = expenses; this.goals = goals; this.festivals = festivals;
    }

    /** Entities -> core domain object. */
    public User loadUser(Long uid) {
        UserEntity ue = users.findById(uid).orElseThrow();
        User u = new User(ue.getName(), ue.getIncome(), ue.getMonthlyBudget(), planFor(ue));
        u.depositToEmergencyFund(ue.getEmergencyFund());
        for (ExpenseEntity e : expenses.findByUserId(uid)) {
            boolean fest = e.getFestivalName() != null;
            u.restoreExpense(fest
                ? new FestivalExpense(e.getId(), e.getAmount(), e.getDescription(), e.getSpentAt(), e.getFestivalName())
                : new Expense(e.getId(), e.getAmount(), Category.valueOf(e.getCategory()), e.getDescription(), e.getSpentAt(), e.isPlanned()));
        }
        goals.findByUserId(uid).forEach(g -> u.addGoal(new Goal(g.getName(), g.getTarget(), g.getSaved(), g.getDeadline())));
        festivals.findByUserId(uid).forEach(f -> u.addFestival(new FestivalPlan(f.getName(), f.getFestivalDate(), f.getBudget(), f.getSaved())));
        return u;
    }

    private SubscriptionPlan planFor(UserEntity ue) {
        return switch (ue.getPlanName()) {                    // polymorphism: callers only see SubscriptionPlan
            case "STUDENT" -> new StudentPlan(ue.getStudentId());
            case "BASIC" -> new BasicPlan();
            default -> new FreePlan();
        };
    }

    public void addExpense(Long uid, double amount, Category category, String description, boolean planned,
                           String festivalName, LocalDateTime when) throws ExpenseLimitReachedException {
        User u = loadUser(uid);
        LocalDateTime time = when != null ? when : LocalDateTime.now();
        String desc = (description == null || description.isBlank()) ? category.name().toLowerCase() : description.trim();
        boolean fest = festivalName != null && !festivalName.isBlank();
        Expense e = fest ? new FestivalExpense(null, amount, desc, time, festivalName.trim())
                         : new Expense(null, amount, category, desc, time, planned);
        u.addExpense(e);                                      // may throw ExpenseLimitReachedException
        expenses.save(new ExpenseEntity(uid, amount, e.getCategory().name(), desc, time, e.isPlanned(), fest ? festivalName.trim() : null));
    }

    public void addGoal(Long uid, String name, double target, double saved, LocalDate deadline) {
        goals.save(new GoalEntity(uid, name.trim(), target, saved, deadline));
    }
    public void addFestival(Long uid, String name, LocalDate date, double budget, double saved) {
        festivals.save(new FestivalEntity(uid, name.trim(), date, budget, saved));
    }
    public void depositEmergency(Long uid, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        UserEntity ue = users.findById(uid).orElseThrow();
        ue.setEmergencyFund(ue.getEmergencyFund() + amount);
        users.save(ue);
    }
    public void updateMoney(Long uid, double income, double budget) {
        if (income <= 0 || budget <= 0) throw new IllegalArgumentException("Income and budget must be positive");
        UserEntity ue = users.findById(uid).orElseThrow();
        ue.setIncome(income); ue.setMonthlyBudget(budget);
        users.save(ue);
    }
    /** Demo "payment": no real money moves. The Student plan needs a student ID (validated by StudentPlan). */
    public void changePlan(Long uid, String plan, String studentId) {
        UserEntity ue = users.findById(uid).orElseThrow();
        if ("STUDENT".equals(plan)) { new StudentPlan(studentId); ue.setStudentId(studentId.trim()); }
        ue.setPlanName(plan);
        users.save(ue);
    }
}
