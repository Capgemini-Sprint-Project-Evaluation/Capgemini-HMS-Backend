package com.capgemini.hms.auth.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO for ADMIN-only user account creation (POST /api/admin/users).
 *
 * Security Design:
 * - Only accessible by authenticated users with ROLE_ADMIN.
 * - Role is server-side validated against an allowed set: {admin, doctor, nurse}.
 * - staffId is required and validated for doctor/nurse roles (must match existing clinical record).
 * - Creating PATIENT accounts via this endpoint is not allowed — patients self-register.
 */
public class AdminCreateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(example = "dr_smith", description = "Unique username for the new staff/admin account")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Must be a valid email address")
    @Schema(example = "dr.smith@hospital.com", description = "Work email address for the account")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    @Schema(example = "tempPass@2026", description = "Temporary password (user should change on first login)")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(
        regexp = "^(admin|doctor|nurse)$",
        message = "Role must be exactly one of: admin, doctor, nurse"
    )
    @Schema(
        example = "doctor",
        description = "Role to assign to the new account. Allowed values: admin, doctor, nurse. " +
                      "Note: 'patient' is not allowed here — patients self-register via /api/v1/auth/signup."
    )
    private String role;

    @Schema(
        example = "101",
        description = "Employee ID of the doctor/nurse to link this account to. " +
                      "Required when role is 'doctor' or 'nurse'. Must match an existing Physician/Nurse record."
    )
    private Integer staffId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}
