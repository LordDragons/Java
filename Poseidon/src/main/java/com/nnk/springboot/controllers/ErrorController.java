package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping
    public String accessDenied(Model model) {
        model.addAttribute("message", "You do not have permission to access this resource");
        return "403";
    }
}
