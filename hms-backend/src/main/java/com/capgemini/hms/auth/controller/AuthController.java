package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.dto.LoginRequest;
import com.capgemini.hms.auth.dto.SignupRequest;
import com.capgemini.hms.auth.service.AuthService;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return ApiResponses.ok("User signed in successfully.", null);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ApiResponses.created("User registered successfully.", authService.registerUser(signUpRequest).getData());
    }

    @GetMapping("/csrf")
    public ResponseEntity<ApiResponse<String>> csrf(CsrfToken csrfToken) {
        return ApiResponses.ok("CSRF token fetched successfully.", csrfToken.getToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ApiResponses.ok("User signed out successfully.", null);
    }
}
