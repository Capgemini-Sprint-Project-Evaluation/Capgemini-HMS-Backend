package com.capgemini.hms.auth.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    @Schema(example = "admin@hms.com", description = "User's registered email address")
    private String email;

    @NotBlank
    @Schema(example = "admin123", description = "User's password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
