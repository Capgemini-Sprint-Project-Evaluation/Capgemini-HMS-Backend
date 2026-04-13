package com.capgemini.hms.stay.controller.api;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.PagedResponse;
import com.capgemini.hms.stay.dto.StayRequest;
import com.capgemini.hms.stay.dto.StayResponse;
import com.capgemini.hms.stay.entity.Stay;
import com.capgemini.hms.stay.service.StayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.capgemini.hms.security.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/stays")
@Tag(name = "Patient Stay Management", description = "Endpoints for managing patient admissions, room assignments, and discharges")
public class StayController {

    private final StayService stayService;

    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @GetMapping("/my")
    @Operation(summary = "Get my stays", description = "Returns all hospital admission records for the current logged-in patient")
    public ResponseEntity<ApiResponse<List<StayResponse>>> getMyStays() {
        Integer ssn = getAuthenticatedPatientSsn();
        List<StayResponse> stays = stayService.getPatientStays(ssn).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(stays));
    }

    @GetMapping
    @Operation(summary = "Get all stays", description = "Returns a paginated list of all active and historical stays")
    public ResponseEntity<ApiResponse<PagedResponse<StayResponse>>> getAllStays(Pageable pageable) {
        Page<Stay> page = stayService.getAllStays(pageable);
        List<StayResponse> content = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        PagedResponse<StayResponse> pagedResponse = new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }

    @PostMapping
    @Operation(summary = "New patient stay (Check-in)", description = "Admits a patient into a specific room")
    public ResponseEntity<ApiResponse<StayResponse>> checkIn(@Valid @RequestBody StayRequest request) {
        Stay stay = stayService.checkInPatient(
                request.getPatientSsn(),
                request.getRoomNumber(),
                request.getStayStart(),
                request.getNotes());
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(stay), "Patient checked in successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stay details", description = "Returns detailed information for a specific stay ID")
    public ResponseEntity<ApiResponse<StayResponse>> getStayById(@PathVariable Integer id) {
        return stayService.getStayById(id)
                .map(s -> ResponseEntity.ok(ApiResponse.success(convertToDTO(s))))
                .orElseThrow(() -> new RuntimeException("Stay record not found with ID: " + id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update or finalize stay (Check-out)", description = "Updates stay notes or records a discharge time")
    public ResponseEntity<ApiResponse<StayResponse>> updateStay(@PathVariable Integer id, @RequestParam(required = false) String notes) {
        Stay stay = stayService.checkOutPatient(id, notes);
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(stay), "Stay details updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stay record", description = "Performs a soft-delete on a stay record")
    public ResponseEntity<ApiResponse<String>> deleteStay(@PathVariable Integer id) {
        stayService.deleteStay(id);
        return ResponseEntity.ok(ApiResponse.success("Stay record deleted successfully"));
    }

    @GetMapping("/active")
    @Operation(summary = "List active inpatients", description = "Returns all patients currently admitted")
    public ResponseEntity<ApiResponse<List<StayResponse>>> getActiveStays() {
        List<StayResponse> active = stayService.getActiveStays().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(active));
    }

    private StayResponse convertToDTO(Stay s) {
        return new StayResponse(
                s.getStayId(),
                s.getPatient().getName(),
                s.getRoom().getRoomNumber(),
                s.getStayStart(),
                s.getStayEnd(),
                s.getNotes());
    }

    private Integer getAuthenticatedPatientSsn() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getPatientSsn() == null) {
            throw new RuntimeException("Your account is not linked to a clinical patient record.");
        }
        return userDetails.getPatientSsn();
    }
}
