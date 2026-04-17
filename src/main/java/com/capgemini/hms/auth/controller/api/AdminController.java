package com.capgemini.hms.auth.controller.api;

import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.payload.request.AdminCreateRequest;
import com.capgemini.hms.auth.payload.response.MessageResponse;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.nurse.repository.NurseRepository;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import com.capgemini.hms.security.SecurityAuditService;
import com.capgemini.hms.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * ADMIN-ONLY controller for managing system user accounts.
 *
 * Security:
 * - All endpoints require ROLE_ADMIN (enforced at both URL level in WebSecurityConfig
 *   and method level via @PreAuthorize for defence-in-depth).
 * - Only ADMIN, DOCTOR, and NURSE accounts can be created here.
 * - PATIENT accounts are created via public self-registration (/api/v1/auth/signup).
 * - Full audit log on every user creation and role assignment.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin User Management",
     description = "ADMIN-ONLY endpoints for creating staff and admin user accounts. " +
                   "Requires an active ADMIN session. All actions are audit-logged.")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private NurseRepository nurseRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private SecurityAuditService auditService;

    /**
     * Creates a new ADMIN, DOCTOR, or NURSE user account.
     *
     * Privilege levels:
     * - admin: Full system access
     * - doctor: Clinical operations, prescriptions, appointments
     * - nurse: Patient check-in, on-call shifts, clinical reads
     *
     * For doctor/nurse: the staffId must match an existing Physician/Nurse clinical record.
     */
    @PostMapping("/users")
    @Operation(
        summary = "Create a staff or admin user account",
        description = "Creates a new user account with role ADMIN, DOCTOR, or NURSE. " +
                      "Requires an active ADMIN session. " +
                      "staffId is REQUIRED for doctor and nurse roles and must match an existing clinical record. " +
                      "All creations are permanently audit-logged."
    )
    public ResponseEntity<?> createUser(
            @Valid @RequestBody AdminCreateRequest request,
            HttpServletRequest httpRequest) {

        String creatorUsername = getCurrentAdminUsername();
        String clientIp = auditService.getClientIp(httpRequest);

        // ── Step 1: Username uniqueness ──────────────────────────────────────
        if (userRepository.existsByUsername(request.getUsername())) {
            auditService.logAuthFailure(request.getUsername(), "USERNAME_TAKEN", clientIp);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username '" + request.getUsername() + "' is already taken."));
        }

        // ── Step 2: Email uniqueness ─────────────────────────────────────────
        if (userRepository.existsByEmail(request.getEmail())) {
            auditService.logAuthFailure(request.getUsername(), "EMAIL_IN_USE", clientIp);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email '" + request.getEmail() + "' is already in use."));
        }

        // ── Step 3: Build User entity ────────────────────────────────────────
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword())
        );

        // ── Step 4: Resolve and validate role ────────────────────────────────
        ERole targetERole;
        switch (request.getRole().toLowerCase()) {

            case "admin":
                targetERole = ERole.ROLE_ADMIN;
                // No staffId required for admin — admin has no clinical record linkage
                break;

            case "doctor":
                targetERole = ERole.ROLE_DOCTOR;
                if (request.getStaffId() == null) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse(
                                "Error: 'staffId' (physician employeeId) is required when creating a DOCTOR account."));
                }
                if (!physicianRepository.existsById(request.getStaffId())) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                            .body(new MessageResponse(
                                "Error: No Physician clinical record found for staffId: " + request.getStaffId() +
                                ". Register the physician first via POST /api/v1/physicians."));
                }
                user.setStaffId(request.getStaffId());
                break;

            case "nurse":
                targetERole = ERole.ROLE_NURSE;
                if (request.getStaffId() == null) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse(
                                "Error: 'staffId' (nurse employeeId) is required when creating a NURSE account."));
                }
                if (!nurseRepository.existsById(request.getStaffId())) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                            .body(new MessageResponse(
                                "Error: No Nurse clinical record found for staffId: " + request.getStaffId() +
                                ". Register the nurse first via POST /api/v1/nurses."));
                }
                user.setStaffId(request.getStaffId());
                break;

            default:
                // Defensive guard — @Pattern validation should have already blocked this
                auditService.logAuthFailure(request.getUsername(),
                        "INVALID_ROLE_ATTEMPT:" + request.getRole(), clientIp);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse(
                            "Error: Invalid role '" + request.getRole() +
                            "'. Allowed values: admin, doctor, nurse. " +
                            "Patient accounts are created via public self-registration."));
        }

        // ── Step 5: Look up and assign the Role entity ───────────────────────
        Role roleEntity = roleRepository.findByName(targetERole)
                .orElseThrow(() -> new RuntimeException(
                    "Critical: Role " + targetERole + " not found in database. " +
                    "Run SystemInitializer to seed roles."));
        user.setRoles(Collections.singleton(roleEntity));

        // ── Step 6: Persist ──────────────────────────────────────────────────
        userRepository.save(user);

        // ── Step 7: Audit trail ──────────────────────────────────────────────
        auditService.logUserCreated(request.getUsername(), request.getRole(), creatorUsername, clientIp);
        auditService.logRoleAssignment(request.getUsername(), request.getRole(), creatorUsername);

        return ResponseEntity.ok(ApiResponse.success(
                "Account created successfully for '" + request.getUsername() +
                "' with role " + request.getRole().toUpperCase() + "."
        ));
    }

    // ── Helper ──────────────────────────────────────────────────────────────

    /**
     * Extracts the username of the currently authenticated admin from the SecurityContext.
     */
    private String getCurrentAdminUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        }
        return "SYSTEM";
    }
}
