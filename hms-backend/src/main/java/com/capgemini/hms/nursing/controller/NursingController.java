package com.capgemini.hms.nursing.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import com.capgemini.hms.nursing.dto.NurseDTO;
import com.capgemini.hms.nursing.service.NursingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nursing")
public class NursingController {
    private static final Logger log = LoggerFactory.getLogger(NursingController.class);

    private final NursingService nursingService;

    public NursingController(NursingService nursingService) {
        this.nursingService = nursingService;
    }

    @GetMapping("/nurses")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<NurseDTO>>> getAllNurses() {
        log.info("Request received: Fetch all nurses");
        return ApiResponses.ok(nursingService.getAllNurses());
    }

    @GetMapping("/nurses/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<NurseDTO>>> searchNurses(@RequestParam String query) {
        log.info("Request received: Search nurses with query: {}", query);
        return ApiResponses.ok(nursingService.searchNurses(query));
    }

    @GetMapping("/nurses/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<NurseDTO>> getNurseById(@PathVariable Integer id) {
        log.info("Request received: Fetch nurse by ID: {}", id);
        return ApiResponses.ok(nursingService.getNurseById(id));
    }

    @PostMapping("/nurses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NurseDTO>> registerNurse(@Valid @RequestBody NurseDTO dto) {
        log.info("Request received: Onboard new nurse: {}", dto.getName());
        return ApiResponses.created("Nurse onboarded successfully.", nursingService.registerNurse(dto));
    }

    @PutMapping("/nurses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NurseDTO>> updateNurse(@PathVariable Integer id, @Valid @RequestBody NurseDTO dto) {
        log.info("Request received: Update nurse profile ID: {}", id);
        return ApiResponses.ok("Nurse profile updated successfully.", nursingService.updateNurse(id, dto));
    }

    @DeleteMapping("/nurses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteNurse(@PathVariable Integer id) {
        log.info("Request received: Delete nurse record ID: {}", id);
        nursingService.deleteNurse(id);
        return ApiResponses.ok("Nurse record successfully soft-deleted.", null);
    }

    @GetMapping("/on-call")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.nursing.dto.OnCallDTO>>> getOnCallRotation() {
        log.info("Request received: Fetch current on-call rotation");
        return ApiResponses.ok(nursingService.getOnCallRotation());
    }
}
