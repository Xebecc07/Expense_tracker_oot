package com.expensetracker.web;

import com.expensetracker.entity.UserEntity;
import com.expensetracker.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @GetMapping("/login") public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String pin, HttpSession session, Model model) {
        try {
            UserEntity u = auth.loginOrRegister(name, pin);
            session.setAttribute("uid", u.getId());
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
}
