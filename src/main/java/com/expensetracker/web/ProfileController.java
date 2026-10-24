package com.expensetracker.web;

import com.expensetracker.core.*;
import com.expensetracker.service.TrackerService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private final TrackerService tracker;
    public ProfileController(TrackerService tracker) { this.tracker = tracker; }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        model.addAttribute("user", tracker.loadUser(HomeController.uid(session)));
        List<SubscriptionPlan> plans = List.of(new FreePlan(), new StudentPlan("demo"), new BasicPlan());
        model.addAttribute("plans", plans);
        model.addAttribute("features", Feature.values());
        return "profile";
    }

    @PostMapping("/profile/plan")
    public String changePlan(HttpSession session, @RequestParam String plan,
                             @RequestParam(defaultValue = "") String studentId, RedirectAttributes ra) {
        try {
            tracker.changePlan(HomeController.uid(session), plan, studentId);
            ra.addFlashAttribute("msg", "Plan changed to " + plan.charAt(0) + plan.substring(1).toLowerCase() + " (demo payment)");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/emergency")
    public String deposit(HttpSession session, @RequestParam double amount, RedirectAttributes ra) {
        try { tracker.depositEmergency(HomeController.uid(session), amount); ra.addFlashAttribute("msg", "Added to emergency fund"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/profile";
    }

    @PostMapping("/profile/money")
    public String money(HttpSession session, @RequestParam double income, @RequestParam double budget, RedirectAttributes ra) {
        try { tracker.updateMoney(HomeController.uid(session), income, budget); ra.addFlashAttribute("msg", "Saved"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/profile";
    }
}
