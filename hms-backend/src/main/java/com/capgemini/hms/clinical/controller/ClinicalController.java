package com.capgemini.hms.clinical.controller;

import com.capgemini.hms.clinical.dto.MedicationDTO;
import com.capgemini.hms.clinical.dto.PrescriptionDTO;
import com.capgemini.hms.clinical.service.ClinicalService;
import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinical")
public class ClinicalController {
    private static final Logger log = LoggerFactory.getLogger(ClinicalController.class);

    private final ClinicalService clinicalService;

    public ClinicalController(ClinicalService clinicalService) {
        this.clinicalService = clinicalService;
    }

    @GetMapping("/medications")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> getAllMedications() {
        log.info("Request received: Fetch all medications");
        return ApiResponses.ok(clinicalService.getAllMedications());
    }

    @GetMapping("/medications/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> searchMedications(@RequestParam String query) {
        log.info("Request received: Search medications with query: {}", query);
        return ApiResponses.ok(clinicalService.searchMedications(query));
    }

    @GetMapping("/medications/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<MedicationDTO>> getMedicationByCode(@PathVariable Integer code) {
        log.info("Request received: Fetch medication by code: {}", code);
        return ApiResponses.ok(clinicalService.getMedicationByCode(code));
    }

    @PostMapping("/medications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicationDTO>> addMedication(@Valid @RequestBody MedicationDTO dto) {
        log.info("Request received: Add new medication: {}", dto.getName());
        return ApiResponses.created("Medication added successfully.", clinicalService.addMedication(dto));
    }

    @PutMapping("/medications/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicationDTO>> updateMedication(@PathVariable Integer code, @Valid @RequestBody MedicationDTO dto) {
        log.info("Request received: Update medication code: {}", code);
        return ApiResponses.ok("Medication updated successfully.", clinicalService.updateMedication(code, dto));
    }

    @DeleteMapping("/medications/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteMedication(@PathVariable Integer code) {
        log.info("Request received: Delete medication code: {}", code);
        clinicalService.deleteMedication(code);
        return ApiResponses.ok("Medication record successfully soft-deleted.", null);
    }

    @PostMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> prescribe(@Valid @RequestBody PrescriptionDTO dto) {
        log.info("Request received: Prescribe medication to patient SSN: {}", dto.getPatientSsn());
        return ApiResponses.created("Prescription issued successfully.", clinicalService.prescribe(dto));
    }

    @GetMapping("/prescriptions/patient/{ssn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getPrescriptionsByPatient(@PathVariable Integer ssn) {
        log.info("Request received: Fetch prescriptions for patient SSN: {}", ssn);
        return ApiResponses.ok(clinicalService.getPrescriptionsByPatient(ssn));
    }
}
