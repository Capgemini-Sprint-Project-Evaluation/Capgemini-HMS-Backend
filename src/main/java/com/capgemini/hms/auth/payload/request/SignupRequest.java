package com.capgemini.hms.auth.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Legacy DTO — retained for backward compatibility with view-layer form bindings.
 *
 * SECURITY NOTE: The 'role' field has been REMOVED from this DTO.
 * Role assignment is now strictly server-side:
 *   - Public signup (POST /api/v1/auth/signup) always assigns ROLE_PATIENT.
 *   - Staff/Admin creation (POST /api/admin/users) requires ADMIN session.
 *
 * If you are building a new feature, use:
 *   - PatientSignupRequest  — for public patient self-registration
 *   - AdminCreateRequest    — for admin-managed account creation
 */
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(example = "john_doe", description = "Unique username for the account")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(example = "john.doe@example.com", description = "Valid email address")
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    @Schema(example = "password123", description = "Secure password (min 6 characters)")
    private String password;

    @Schema(example = "100000001", description = "SSN of the patient (Required if registering as patient)")
    private Integer patientSsn;

    @Schema(example = "201", description = "Employee ID of the doctor/nurse (Required for staff roles)")
    private Integer staffId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}
