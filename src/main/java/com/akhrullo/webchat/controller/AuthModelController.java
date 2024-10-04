package com.akhrullo.webchat.controller;

import com.akhrullo.webchat.auth.AuthenticationRequest;
import com.akhrullo.webchat.auth.AuthenticationResponse;
import com.akhrullo.webchat.auth.AuthenticationService;
import com.akhrullo.webchat.auth.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthModelController {
    private final AuthenticationService authenticationService;

    public AuthModelController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        model.addAttribute("errorMessage", ""); // Initialize with an empty error message
        return "login"; // Name of the Thymeleaf template
    }

    @PostMapping("/login")
    public String authenticate(
            @Valid @ModelAttribute("authenticationRequest") AuthenticationRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid request");
            return "login"; // Return to the login page with errors
        }

        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            // Store user session details or JWT if needed
            return "redirect:/chat/2"; // Redirect to the chat page or another appropriate page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "login"; // Return to login with an error message
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("errorMessage", ""); // Initialize with an empty error message
        return "register"; // Name of the Thymeleaf template for registration
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid request");
            return "register"; // Return to the registration page with errors
        }

        try {
            authenticationService.register(request);
            return "redirect:/login"; // Redirect to the login page after successful registration
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage()); // Show the exception message
            return "register"; // Return to registration with an error message
        }
    }
}
