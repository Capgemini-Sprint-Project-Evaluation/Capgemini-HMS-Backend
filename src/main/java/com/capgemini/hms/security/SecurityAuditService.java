package com.capgemini.hms.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Centralized audit logging service for all security-sensitive events.
 *
 * Writes structured log entries to a dedicated "SECURITY_AUDIT" logger.
 * Configure this logger separately in logback.xml for long-term retention.
 *
 * Audit events logged:
 * - USER_CREATED    : Any new user account creation (public or admin)
 * - ROLE_ASSIGNED   : Explicit role assignment by an admin
 * - AUTH_FAILURE    : Duplicate username/email, invalid SSN, access denied attempts
 * - PRIVILEGE_ATTEMPT : Any attempt to escalate privileges via public APIs
 */
@Service
public class SecurityAuditService {

    private static final Logger auditLog = LoggerFactory.getLogger("SECURITY_AUDIT");

    /**
     * Logs a new user account creation event.
     *
     * @param username   The username of the newly created account
     * @param role       The role assigned to the account
     * @param createdBy  "PUBLIC_SIGNUP" for self-registration, or admin username for admin creation
     * @param ipAddress  The client's IP address
     */
    public void logUserCreated(String username, String role, String createdBy, String ipAddress) {
        auditLog.info("[AUDIT] USER_CREATED | username={} | role={} | createdBy={} | ip={}",
                username, role.toUpperCase(), createdBy, ipAddress);
    }

    /**
     * Logs a failed authentication or authorization attempt.
     *
     * @param username   The username involved in the failure
     * @param reason     Failure reason (e.g. USERNAME_TAKEN, INVALID_PATIENT_SSN, ACCESS_DENIED)
     * @param ipAddress  The client's IP address
     */
    public void logAuthFailure(String username, String reason, String ipAddress) {
        auditLog.warn("[AUDIT] AUTH_FAILURE | username={} | reason={} | ip={}",
                username, reason, ipAddress);
    }

    /**
     * Logs an explicit role assignment action performed by an admin.
     *
     * @param username    The account receiving the role
     * @param role        The role being assigned
     * @param assignedBy  Username of the admin who performed the action
     */
    public void logRoleAssignment(String username, String role, String assignedBy) {
        auditLog.info("[AUDIT] ROLE_ASSIGNED | username={} | role={} | assignedBy={}",
                username, role.toUpperCase(), assignedBy);
    }

    /**
     * Logs a privilege escalation attempt via the public signup API.
     * This is triggered when a client sends a restricted role (admin/doctor/nurse)
     * via the patient-only public endpoint.
     *
     * @param username     The attempted username
     * @param attemptedRole The role the client tried to inject
     * @param ipAddress   The client's IP address
     */
    public void logPrivilegeEscalationAttempt(String username, String attemptedRole, String ipAddress) {
        auditLog.warn("[AUDIT] PRIVILEGE_ESCALATION_ATTEMPT | username={} | attemptedRole={} | ip={} | " +
                      "ACTION=Role field ignored; PATIENT role assigned instead",
                username, attemptedRole, ipAddress);
    }

    /**
     * Extracts the real client IP address, respecting reverse proxies.
     * Checks X-Forwarded-For header before falling back to remote address.
     */
    public String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
