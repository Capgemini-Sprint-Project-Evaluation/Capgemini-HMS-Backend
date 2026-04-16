package com.capgemini.hms.auth.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * View controller for Auth pages.
 * login is handled via standard Form POST to /login.
 * signup is handled via AJAX to /api/v1/auth/signup (CSRF protected).
 */
@Controller
public class AuthViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }
}
