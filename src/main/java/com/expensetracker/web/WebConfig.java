package com.expensetracker.web;

import jakarta.servlet.http.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

/** Session-based login check: every page except login/static files needs a logged-in user. */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
                if (req.getSession().getAttribute("uid") == null) { res.sendRedirect("/login"); return false; }
                return true;
            }
        }).excludePathPatterns("/login", "/css/**", "/js/**", "/icons/**", "/manifest.json", "/sw.js", "/offline.html", "/error");
    }
}
