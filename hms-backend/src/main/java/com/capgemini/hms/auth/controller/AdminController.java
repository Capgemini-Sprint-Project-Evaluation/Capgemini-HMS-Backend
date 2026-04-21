package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.dto.DashboardSummaryDTO;
import com.capgemini.hms.auth.service.AdminService;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getDashboardSummary() {
        return ApiResponses.ok(adminService.getDashboardSummary());
    }

    @GetMapping("/dashboard/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.auth.dto.DepartmentStatsDTO>>> getDepartmentStats() {
        log.info("Request received: Fetch department statistics");
        return ApiResponses.ok(adminService.getDepartmentStats());
    }

    // --- AFFILIATIONS ---

    @GetMapping("/departments/{id}/physicians")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.auth.dto.AffiliationDTO>>> getAffiliations(@PathVariable Integer id) {
        log.info("Request received: Fetch physicians for department ID: {}", id);
        return ApiResponses.ok(adminService.getAffiliationsByDepartment(id));
    }

    @PostMapping("/departments/affiliate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<com.capgemini.hms.auth.dto.AffiliationDTO>> addAffiliation(@Valid @RequestBody com.capgemini.hms.auth.dto.AffiliationDTO dto) {
        log.info("Request received: Add physician-department affiliation");
        return ApiResponses.created("Physician successfully affiliated with department.", adminService.addAffiliation(dto));
    }

    // --- SECURITY & AUDIT ---

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.auth.entity.User>>> getAllUsers() {
        log.info("Request received: Fetch all system users");
        return ApiResponses.ok(adminService.getAllUsers());
    }

    @GetMapping("/audit/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.auth.dto.AuditLogDTO>>> getAuditLogs() {
        log.info("Request received: Fetch system audit logs");
        return ApiResponses.ok(adminService.getAuditLogs());
    }
}
