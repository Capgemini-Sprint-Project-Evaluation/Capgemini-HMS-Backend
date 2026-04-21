package com.capgemini.hms.scheduling.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import com.capgemini.hms.scheduling.dto.AppointmentDTO;
import com.capgemini.hms.scheduling.dto.PhysicianDTO;
import com.capgemini.hms.scheduling.service.SchedulingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scheduling")
public class SchedulingController {
    private static final Logger log = LoggerFactory.getLogger(SchedulingController.class);

    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    // --- PHYSICIANS ---

    @GetMapping("/physicians")
    public ResponseEntity<ApiResponse<List<PhysicianDTO>>> getAllPhysicians() {
        log.info("Request received: Fetch all physicians");
        return ApiResponses.ok(schedulingService.getAllPhysicians());
    }

    @GetMapping("/physicians/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<PhysicianDTO>>> searchPhysicians(@RequestParam String query) {
        log.info("Request received: Search physicians with query: {}", query);
        return ApiResponses.ok(schedulingService.searchPhysicians(query));
    }

    @GetMapping("/physicians/{id}")
    public ResponseEntity<ApiResponse<PhysicianDTO>> getPhysicianById(@PathVariable Integer id) {
        log.info("Request received: Fetch physician by ID: {}", id);
        return ApiResponses.ok(schedulingService.getPhysicianById(id));
    }

    @PostMapping("/physicians")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PhysicianDTO>> addPhysician(@Valid @RequestBody PhysicianDTO dto) {
        log.info("Request received: Onboard new physician: {}", dto.getName());
        return ApiResponses.created("Physician onboarded successfully.", schedulingService.addPhysician(dto));
    }

    @PutMapping("/physicians/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PhysicianDTO>> updatePhysician(@PathVariable Integer id, @Valid @RequestBody PhysicianDTO dto) {
        log.info("Request received: Update physician profile ID: {}", id);
        return ApiResponses.ok("Physician profile updated successfully.", schedulingService.updatePhysician(id, dto));
    }

    @DeleteMapping("/physicians/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePhysician(@PathVariable Integer id) {
        log.info("Request received: Delete physician record ID: {}", id);
        schedulingService.deletePhysician(id);
        return ApiResponses.ok("Physician record successfully soft-deleted.", null);
    }

    // --- CERTIFICATIONS ---

    @GetMapping("/physicians/{id}/trained-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.scheduling.dto.TrainedInDTO>>> getCertifications(@PathVariable Integer id) {
        log.info("Request received: Fetch certifications for physician ID: {}", id);
        return ApiResponses.ok(schedulingService.getCertificationsByPhysician(id));
    }

    @PostMapping("/physicians/trained-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<com.capgemini.hms.scheduling.dto.TrainedInDTO>> addCertification(@Valid @RequestBody com.capgemini.hms.scheduling.dto.TrainedInDTO dto) {
        log.info("Request received: Add new physician certification");
        return ApiResponses.created("Physician certification successfully recorded.", schedulingService.addCertification(dto));
    }

    // --- APPOINTMENTS ---

    @GetMapping("/appointments")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getAllAppointments() {
        log.info("Request received: Fetch all scheduled appointments");
        return ApiResponses.ok(schedulingService.getAllAppointments());
    }

    @GetMapping("/appointments/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> searchAppointments(@RequestParam String room) {
        log.info("Request received: Search appointments in room: {}", room);
        return ApiResponses.ok(schedulingService.searchAppointments(room));
    }

    @PutMapping("/appointments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(@PathVariable Integer id, @Valid @RequestBody AppointmentDTO dto) {
        log.info("Request received: Update appointment ID: {}", id);
        return ApiResponses.ok("Appointment details updated successfully.", schedulingService.updateAppointment(id, dto));
    }

    @PostMapping("/appointments")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentDTO>> bookAppointment(@Valid @RequestBody AppointmentDTO dto) {
        log.info("Request received: Book appointment for patient SSN: {}", dto.getPatientSsn());
        return ApiResponses.created("Appointment booked successfully.", schedulingService.bookAppointment(dto));
    }

    @DeleteMapping("/appointments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'PATIENT')")
    public ResponseEntity<ApiResponse<String>> cancelAppointment(@PathVariable Integer id) {
        log.info("Request received: Cancel appointment ID: {}", id);
        schedulingService.cancelAppointment(id);
        return ApiResponses.ok("Appointment cancelled successfully.", null);
    }
}
