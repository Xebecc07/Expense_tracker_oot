package com.expensetracker.web;

import com.expensetracker.service.TrackerService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GoalController {
    private final TrackerService tracker;
    public GoalController(TrackerService tracker) { this.tracker = tracker; }

    @GetMapping("/goals")
    public String goals(HttpSession session, Model model) {
        model.addAttribute("user", tracker.loadUser(HomeController.uid(session)));
        return "goals";
    }

    @PostMapping("/goals")
    public String addGoal(HttpSession session, @RequestParam String name, @RequestParam double target,
                          @RequestParam(defaultValue = "0") double saved,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline, RedirectAttributes ra) {
        tracker.addGoal(HomeController.uid(session), name, target, saved, deadline);
        ra.addFlashAttribute("msg", "Goal added");
        return "redirect:/goals";
    }

    @PostMapping("/festivals")
    public String addFestival(HttpSession session, @RequestParam String name, @RequestParam double budget,
                              @RequestParam(defaultValue = "0") double saved,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, RedirectAttributes ra) {
        tracker.addFestival(HomeController.uid(session), name, date, budget, saved);
        ra.addFlashAttribute("msg", "Festival added");
        return "redirect:/goals";
    }
}
