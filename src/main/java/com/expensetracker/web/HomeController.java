package com.expensetracker.web;

import com.expensetracker.core.*;
import com.expensetracker.service.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Comparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    private final TrackerService tracker;
    private final SeedService seeder;
    public HomeController(TrackerService tracker, SeedService seeder) { this.tracker = tracker; this.seeder = seeder; }

    static Long uid(HttpSession s) { return (Long) s.getAttribute("uid"); }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User u = tracker.loadUser(uid(session));
        double spent = u.recentSpend();
        long pct = Math.round(spent / u.getMonthlyBudget() * 100);
        model.addAttribute("user", u);
        model.addAttribute("spent", spent);
        model.addAttribute("pct", pct);
        model.addAttribute("barWidth", Math.min(100, pct));
        model.addAttribute("streak", new StreakTracker().generate(u));
        model.addAttribute("recent", u.getExpenses().stream()
                .sorted(Comparator.comparing(Expense::getTime).reversed()).limit(8).toList());
        return "home";
    }

    @PostMapping("/expenses")
    public String addExpense(HttpSession session, @RequestParam double amount, @RequestParam Category category,
                             @RequestParam(defaultValue = "") String description,
                             @RequestParam(defaultValue = "false") boolean planned,
                             @RequestParam(defaultValue = "") String festivalName,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime when,
                             RedirectAttributes ra) throws ExpenseLimitReachedException {
        try {
            tracker.addExpense(uid(session), amount, category, description, planned, festivalName, when);
            ra.addFlashAttribute("msg", "Expense added");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";     // ExpenseLimitReachedException is handled in GlobalAdvice
    }

    @PostMapping("/seed")
    public String seed(HttpSession session, RedirectAttributes ra) {
        seeder.seed(uid(session));
        ra.addFlashAttribute("msg", "Demo data loaded (Basic plan)");
        return "redirect:/";
    }
}
