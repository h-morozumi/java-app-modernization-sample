package com.example.legacy.controller;

import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller.
 *
 * ⚠ Migration Issue (Spring Boot 3):
 *   - Trailing slash: "/home/" matches "/home" in Boot 2, NOT in Boot 3
 */
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("bootVersion", SpringBootVersion.getVersion());
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
