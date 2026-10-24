package com.expensetracker.web;

import com.expensetracker.core.*;
import com.expensetracker.service.TrackerService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InsightController {
    private final TrackerService tracker;
    public InsightController(TrackerService tracker) { this.tracker = tracker; }

    @GetMapping("/insights")
    public String insights(@RequestParam(defaultValue = "LOGGING") StreakType streak, HttpSession session, Model model) {
        User u = tracker.loadUser(HomeController.uid(session));
        StreakTracker streaks = new StreakTracker();
        // POLYMORPHISM: every feature is handled through the Insight interface.
        List<Insight> all = List.of(new CategoryReport(), new ImpulseDetector(), new HiddenExpenseFinder(),
                new GoalImpactCalculator(), streaks, new FestivalPlanner(), new EmergencySavings(), new ExpenseStory());
        List<InsightView> views = all.stream().map(i -> {
            boolean ok = u.getPlan().allows(i.getRequiredFeature());
            String text = !ok ? "" : (i == streaks ? streaks.generate(u, streak) : i.run(u));
            String needs = new StudentPlan("x").allows(i.getRequiredFeature()) ? "Student" : "Basic";
            return new InsightView(i.getName(), ok, text, i == streaks, needs);
        }).toList();
        model.addAttribute("views", views);
        model.addAttribute("streakTypes", StreakType.values());
        model.addAttribute("selected", streak);
        return "insights";
    }
}
