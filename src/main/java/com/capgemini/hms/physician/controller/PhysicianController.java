package com.capgemini.hms.physician.controller;

import com.capgemini.hms.common.service.StaffService;
import com.capgemini.hms.physician.dto.PhysicianDTO;
import com.capgemini.hms.physician.entity.Physician;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/physicians")
@Tag(name = "Physician Management", description = "Endpoints for managing physicians and their profiles")
public class PhysicianController {

    private final StaffService staffService;

    public PhysicianController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    @Operation(summary = "Get all physicians", description = "Returns a paginated list of all active physicians")
    public ResponseEntity<ApiResponse<PagedResponse<PhysicianDTO>>> getAllPhysicians(Pageable pageable) {
        Page<Physician> page = staffService.getAllPhysicians(pageable);
        List<PhysicianDTO> content = page.getContent().stream()
                .map(p -> new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn()))
                .collect(Collectors.toList());
        PagedResponse<PhysicianDTO> pagedResponse = new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get physician by ID", description = "Returns profile details for a specific active physician")
    public ResponseEntity<ApiResponse<PhysicianDTO>> getPhysicianById(@PathVariable Integer id) {
        return staffService.getPhysicianById(id)
                .map(p -> ResponseEntity.ok(ApiResponse.success(new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn()))))
                .orElseThrow(() -> new RuntimeException("Physician not found with ID: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new physician profile", description = "Adds a new physician record")
    public ResponseEntity<ApiResponse<PhysicianDTO>> createPhysician(@Valid @RequestBody PhysicianDTO physicianDTO) {
        Physician physician = new Physician(
                physicianDTO.getEmployeeId(),
                physicianDTO.getName(),
                physicianDTO.getPosition(),
                physicianDTO.getSsn()
        );
        Physician saved = staffService.savePhysician(physician);
        return ResponseEntity.ok(ApiResponse.success(new PhysicianDTO(saved.getEmployeeId(), saved.getName(), saved.getPosition(), saved.getSsn()), "Physician created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update physician profile", description = "Updates an existing physician record")
    public ResponseEntity<ApiResponse<PhysicianDTO>> updatePhysician(@PathVariable Integer id, @Valid @RequestBody PhysicianDTO physicianDTO) {
        physicianDTO.setEmployeeId(id);
        Physician physician = new Physician(
                physicianDTO.getEmployeeId(),
                physicianDTO.getName(),
                physicianDTO.getPosition(),
                physicianDTO.getSsn()
        );
        Physician updated = staffService.updatePhysician(physician);
        return ResponseEntity.ok(ApiResponse.success(new PhysicianDTO(updated.getEmployeeId(), updated.getName(), updated.getPosition(), updated.getSsn()), "Physician updated successfully"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    @Operation(summary = "Search physicians", description = "Filters active physicians by name or position with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<PhysicianDTO>>> searchPhysicians(@RequestParam String query, Pageable pageable) {
        Page<Physician> page = staffService.searchPhysicians(query, pageable);
        List<PhysicianDTO> content = page.getContent().stream()
                .map(p -> new PhysicianDTO(p.getEmployeeId(), p.getName(), p.getPosition(), p.getSsn()))
                .collect(Collectors.toList());
        PagedResponse<PhysicianDTO> pagedResponse = new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove physician record", description = "Performs a soft-delete on a physician profile")
    public ResponseEntity<ApiResponse<String>> deletePhysician(@PathVariable Integer id) {
        staffService.deletePhysician(id);
        return ResponseEntity.ok(ApiResponse.success("Physician record deleted successfully"));
    }
}
