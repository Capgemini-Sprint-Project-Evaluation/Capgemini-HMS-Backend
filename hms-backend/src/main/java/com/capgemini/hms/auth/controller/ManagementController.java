package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.service.ManagementService;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/management")
@Slf4j
@RequiredArgsConstructor
public class ManagementController {
    
    private final ManagementService managementService;

    @GetMapping("/departments")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE','PATIENT')")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        log.info("Request received: Fetch all hospital departments");
        return ApiResponses.ok(managementService.getAllDepartments());
    }

    @GetMapping("/departments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE','PATIENT')")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable Integer id) {
        log.info("Request received: Fetch department by ID: {}", id);
        return ApiResponses.ok(managementService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Department>> createDepartment(@Valid @RequestBody Department department) {
        log.info("Request received: Create new department: {}", department.getName());
        return ApiResponses.created("Department created successfully.", managementService.saveDepartment(department));
    }

    @PutMapping("/departments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable Integer id, @Valid @RequestBody Department department) {
        log.info("Request received: Update department ID: {}", id);
        return ApiResponses.ok("Department updated successfully.", managementService.updateDepartment(id, department));
    }

    @DeleteMapping("/departments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable Integer id) {
        log.info("Request received: Delete department ID: {}", id);
        managementService.deleteDepartment(id);
        return ApiResponses.ok("Department deleted successfully.", null);
    }
}
