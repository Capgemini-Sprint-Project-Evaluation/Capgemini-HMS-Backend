package com.capgemini.hms.prescription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PrescriptionRequest {
    @NotNull
    @Schema(example = "101", description = "Employee ID of the prescribing physician")
    private Integer physicianId;

    @NotNull
    @Schema(example = "100000001", description = "SSN of the patient")
    private Integer patientSsn;

    @NotNull
    @Schema(example = "1", description = "Unique code of the medication")
    private Integer medicationCode;

    @NotNull
    @Schema(example = "2026-04-10T09:00:00", description = "Timestamp of the prescription")
    private LocalDateTime date;

    @Schema(example = "1", description = "ID of the appointment during which the medication was prescribed (optional)")
    private Integer appointmentId; // Optional

    @NotBlank
    @Schema(example = "500mg twice daily for 7 days", description = "Dosage instructions")
    private String dose;

    public PrescriptionRequest() {}

    public PrescriptionRequest(Integer physicianId, Integer patientSsn, Integer medicationCode, 
                               LocalDateTime date, Integer appointmentId, String dose) {
        this.physicianId = physicianId;
        this.patientSsn = patientSsn;
        this.medicationCode = medicationCode;
        this.date = date;
        this.appointmentId = appointmentId;
        this.dose = dose;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getMedicationCode() { return medicationCode; }
    public void setMedicationCode(Integer medicationCode) { this.medicationCode = medicationCode; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
}
