package com.capgemini.hms.auth.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO for PUBLIC patient self-registration (POST /api/v1/auth/signup).
 *
 * Security Design:
 * - The 'role' field is intentionally ABSENT from this DTO.
 * - Role is ALWAYS assigned server-side as ROLE_PATIENT — the client has zero control.
 * - Any extra/unrecognized JSON fields (e.g. "role") sent by the client are silently ignored.
 * - patientSsn MUST link to an existing clinical patient record created by hospital staff.
 */
public class PatientSignupRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(example = "john_doe", description = "Unique username for login (3–20 characters)")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Must be a valid email address")
    @Schema(example = "john.doe@example.com", description = "Valid email address (used for account recovery)")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    @Schema(example = "securePass@2026", description = "Account password (min 6 characters)")
    private String password;

    @NotNull(message = "Patient SSN is required to link your clinical record")
    @Schema(
        example = "100000001",
        description = "SSN of your existing clinical patient record. " +
                      "Your patient record must be pre-registered by hospital staff before you can create an account."
    )
    private Integer patientSsn;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }
}
