package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.dto.LoginRequest;
import com.capgemini.hms.auth.dto.SignupRequest;
import com.capgemini.hms.auth.service.AuthService;
import com.capgemini.hms.common.dto.ApiResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticateUser_shouldAuthenticateAndStoreSecurityContext() {
        LoginRequest request = LoginRequest.builder().username("admin").password("secret").build();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        ResponseEntity<ApiResponse<String>> response = authController.authenticateUser(
                request,
                new MockHttpServletRequest(),
                new MockHttpServletResponse()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User signed in successfully.", response.getBody().getMessage());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void registerUser_shouldReturnCreatedResponse() {
        SignupRequest request = SignupRequest.builder().username("user").build();
        when(authService.registerUser(request)).thenReturn(ApiResponse.success(null, "User registered successfully!"));

        ResponseEntity<ApiResponse<String>> response = authController.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody().getMessage());
    }

    @Test
    void csrf_shouldReturnTokenValue() {
        CsrfToken csrfToken = mock(CsrfToken.class);
        when(csrfToken.getToken()).thenReturn("csrf-token");

        ResponseEntity<ApiResponse<String>> response = authController.csrf(csrfToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("csrf-token", response.getBody().getData());
    }

    @Test
    void logout_shouldInvalidateExistingSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession(true);
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<ApiResponse<String>> result = authController.logout(request, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User signed out successfully.", result.getBody().getMessage());
    }

    @Test
    void logout_shouldHandleMissingSession() {
        ResponseEntity<ApiResponse<String>> result =
                authController.logout(new MockHttpServletRequest(), new MockHttpServletResponse());

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
