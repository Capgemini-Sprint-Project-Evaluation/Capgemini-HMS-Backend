package com.capgemini.hms.patient.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import com.capgemini.hms.patient.dto.*;
import com.capgemini.hms.patient.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllPatients() {
        log.info("Request received: Fetch all patients");
        return ApiResponses.ok(patientService.getAllPatients());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<PatientDTO>>> searchPatients(@RequestParam String query) {
        log.info("Request received: Search patients with query: {}", query);
        return ApiResponses.ok(patientService.searchPatients(query));
    }

    @GetMapping("/{ssn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientBySsn(@PathVariable Integer ssn) {
        log.info("Request received: Fetch patient by SSN: {}", ssn);
        return ApiResponses.ok(patientService.getPatientBySsn(ssn));
    }

    @GetMapping("/{ssn}/stays")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<StayDTO>>> getStayHistory(@PathVariable Integer ssn) {
        log.info("Request received: Fetch stay history for patient SSN: {}", ssn);
        return ApiResponses.ok(patientService.getStayHistory(ssn));
    }

    @GetMapping("/stays/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<StayDTO>>> getActiveStays() {
        log.info("Request received: Fetch active patient admissions");
        return ApiResponses.ok(patientService.getActiveStays());
    }

    @PostMapping("/stays")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<StayDTO>> recordStay(@Valid @RequestBody StayDTO dto) {
        log.info("Request received: Record new patient stay for SSN: {}", dto.getPatientSsn());
        return ApiResponses.created("Patient stay recorded successfully.", patientService.recordStay(dto));
    }

    @PutMapping("/stays/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<StayDTO>> updateStay(@PathVariable Integer id, @Valid @RequestBody StayDTO dto) {
        log.info("Request received: Update stay record ID: {}", id);
        return ApiResponses.ok("Stay record updated successfully.", patientService.updateStay(id, dto));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<PatientDTO>> savePatient(@Valid @RequestBody PatientDTO dto) {
        log.info("Request received: Save patient record: {}", dto.getName());
        return ApiResponses.created("Patient record saved successfully.", patientService.savePatient(dto));
    }

    @PutMapping("/{ssn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<PatientDTO>> updatePatient(@PathVariable Integer ssn, @Valid @RequestBody PatientDTO dto) {
        log.info("Request received: Update patient record SSN: {}", ssn);
        return ApiResponses.ok("Patient record updated successfully.", patientService.updatePatient(ssn, dto));
    }

    @DeleteMapping("/{ssn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePatient(@PathVariable Integer ssn) {
        log.info("Request received: Delete patient record SSN: {}", ssn);
        patientService.deletePatient(ssn);
        return ApiResponses.ok("Patient record successfully soft-deleted.", null);
    }

    // --- PROCEDURES ---

    @GetMapping("/procedures")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<ProcedureDTO>>> getAllProcedures() {
        log.info("Request received: Fetch all clinical procedures");
        return ApiResponses.ok(patientService.getAllProcedures());
    }

    @GetMapping("/procedures/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<ProcedureDTO>>> searchProcedures(@RequestParam String query) {
        log.info("Request received: Search procedures with query: {}", query);
        return ApiResponses.ok(patientService.searchProcedures(query));
    }

    @GetMapping("/{ssn}/procedures")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<ProcedureDTO>>> getPatientProcedures(@PathVariable Integer ssn) {
        log.info("Request received: Fetch procedures for patient SSN: {}", ssn);
        return ApiResponses.ok(patientService.getProceduresByPatient(ssn));
    }

    @PostMapping("/procedures")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProcedureDTO>> addProcedure(@Valid @RequestBody ProcedureDTO dto) {
        log.info("Request received: Add new clinical procedure: {}", dto.getName());
        return ApiResponses.created("Procedure added successfully.", patientService.addProcedure(dto));
    }

    // --- UNDERGOES ---

    @GetMapping("/undergoes/{ssn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<UndergoesDTO>>> getUndergoesHistory(@PathVariable Integer ssn) {
        log.info("Request received: Fetch procedure history for patient SSN: {}", ssn);
        return ApiResponses.ok(patientService.getUndergoesHistory(ssn));
    }

    @PostMapping("/undergoes")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<UndergoesDTO>> recordProcedure(@Valid @RequestBody UndergoesDTO dto) {
        log.info("Request received: Record new clinical procedure for patient: {}", dto.getPatientSsn());
        return ApiResponses.created("Clinical procedure recorded successfully.", patientService.recordProcedure(dto));
    }
}
