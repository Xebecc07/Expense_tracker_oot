package com.expensetracker.web;

import com.expensetracker.core.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/** Shared model data + the custom checked exception turned into a friendly upgrade prompt. */
@ControllerAdvice
public class GlobalAdvice {
    @ModelAttribute("categories")
    public Category[] categories() { return Category.values(); }

    @ExceptionHandler(ExpenseLimitReachedException.class)
    public ModelAndView limitReached(ExpenseLimitReachedException ex) {
        ModelAndView mv = new ModelAndView("limit");
        mv.addObject("message", ex.getMessage());
        mv.addObject("categories", Category.values());
        return mv;
    }
}
