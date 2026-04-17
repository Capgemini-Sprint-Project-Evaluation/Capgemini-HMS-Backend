package com.capgemini.hms.auth.controller.api;

import com.capgemini.hms.auth.entity.ERole;
import com.capgemini.hms.auth.entity.Role;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.payload.request.PatientSignupRequest;
import com.capgemini.hms.auth.payload.response.MessageResponse;
import com.capgemini.hms.auth.repository.RoleRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.security.SecurityAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Public endpoint for patient self-registration.
 *
 * Security Model:
 * ─────────────────────────────────────────────────────────────────────────────
 * This controller ONLY creates PATIENT accounts. The role is HARDCODED server-side.
 * No client-provided field can influence role assignment.
 *
 * For ADMIN / DOCTOR / NURSE account creation → see AdminController (POST /api/admin/users).
 *
 * Flow:
 *   1. Validate username and email uniqueness.
 *   2. Validate patientSsn links to an existing clinical patient record.
 *   3. Create User with ROLE_PATIENT (hardcoded — zero client control).
 *   4. Emit audit log entry.
 *   5. Return 200 OK.
 * ─────────────────────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Patient Self-Registration",
     description = "Public endpoint for patients to create their own portal account. " +
                   "Role is ALWAYS set to PATIENT server-side — clients cannot influence role assignment. " +
                   "Staff/Admin accounts are created by ADMIN via POST /api/admin/users.")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private SecurityAuditService auditService;

    /**
     * Registers a new patient portal account.
     *
     * The patientSsn must correspond to an existing clinical patient record created
     * by hospital staff (ADMIN/NURSE via POST /api/v1/patients). This ensures that
     * only real, hospital-registered patients can create accounts.
     *
     * SECURITY NOTE: The role is unconditionally set to ROLE_PATIENT on the server.
     * If a client sends a "role" field in the JSON body, it is silently ignored by
     * Jackson's default behaviour — no privilege escalation is possible.
     */
    @PostMapping("/signup")
    @Operation(
        summary = "Register a patient account (self-service)",
        description = "Creates a PATIENT portal account linked to an existing clinical record. " +
                      "Role is always PATIENT — this cannot be changed by the client. " +
                      "To create ADMIN/DOCTOR/NURSE accounts, use POST /api/admin/users (requires ADMIN session)."
    )
    public ResponseEntity<?> registerPatient(
            @Valid @RequestBody PatientSignupRequest signUpRequest,
            HttpServletRequest httpRequest) {

        String clientIp = auditService.getClientIp(httpRequest);

        // ── Step 1: Username uniqueness ──────────────────────────────────────
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            auditService.logAuthFailure(signUpRequest.getUsername(), "USERNAME_TAKEN", clientIp);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken."));
        }

        // ── Step 2: Email uniqueness ─────────────────────────────────────────
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            auditService.logAuthFailure(signUpRequest.getUsername(), "EMAIL_IN_USE", clientIp);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use."));
        }

        // ── Step 3: Clinical record linkage validation ───────────────────────
        // The patient must already be registered in the hospital's clinical system
        // by a nurse or admin. Self-registration without a prior clinical record is blocked.
        if (!patientRepository.existsById(signUpRequest.getPatientSsn())) {
            auditService.logAuthFailure(
                    signUpRequest.getUsername(),
                    "INVALID_PATIENT_SSN:" + signUpRequest.getPatientSsn(),
                    clientIp);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                        "Error: No clinical patient record found for SSN: " + signUpRequest.getPatientSsn() +
                        ". Please contact the hospital reception to register as a patient first."));
        }

        // ── Step 4: Build User — role is HARDCODED to PATIENT ────────────────
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );
        user.setPatientSsn(signUpRequest.getPatientSsn());

        Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                .orElseThrow(() -> new RuntimeException(
                    "Critical: ROLE_PATIENT not found in database. Contact the system administrator."));
        user.setRoles(Collections.singleton(patientRole));

        // ── Step 5: Persist ──────────────────────────────────────────────────
        userRepository.save(user);

        // ── Step 6: Audit trail ──────────────────────────────────────────────
        auditService.logUserCreated(signUpRequest.getUsername(), "PATIENT", "PUBLIC_SIGNUP", clientIp);

        return ResponseEntity.ok(ApiResponse.success(
                "Patient account registered successfully. You can now log in at /login."
        ));
    }
}
