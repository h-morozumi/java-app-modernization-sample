package com.example.legacy.controller;

import com.example.legacy.model.UserForm;
import com.example.legacy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Thymeleaf-based user controller using javax.validation.
 *
 * ⚠ Migration Issues (Spring Boot 3):
 *   - javax.validation.* → jakarta.validation.*
 *   - Trailing slash: GET /users/ matches in Boot 2, NOT in Boot 3
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userForm", new UserForm());
        return "users";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "user-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute UserForm userForm,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "users";
        }
        userService.create(userForm);
        redirectAttributes.addFlashAttribute("message", "User created successfully!");
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        return "redirect:/users";
    }
}
