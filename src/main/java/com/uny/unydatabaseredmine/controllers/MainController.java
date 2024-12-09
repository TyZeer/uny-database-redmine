package com.uny.unydatabaseredmine.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public String showHomePage(Authentication authentication, Model model) {
        String username = authentication.getName(); // Получаем имя пользователя
        model.addAttribute("username", username);
        return "index";
    }
}
