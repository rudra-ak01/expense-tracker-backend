package com.expensetracker.controller;

import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.exception.ResourceAlreadyExistsException;
import com.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

   private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        model.addAttribute("activePage", "transactions");
        return "transactions";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("activePage", "categories");
        return "categories";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Display registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    // Process registration form
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Check whether passwords match
        if (!request.getPassword().equals(confirmPassword)) {
            result.rejectValue(
                    "password",
                    "password.mismatch",
                    "Passwords do not match"
            );
        }

        // Return form if validation fails
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(request);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Registration successful! Please sign in."
            );

            return "redirect:/login";

        } catch (ResourceAlreadyExistsException ex) {
            result.rejectValue("email", "email.exists", ex.getMessage());
            return "auth/register";
        }
    }
}