package com.capgemini.hms.patient.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class UndergoesDTO {
    @NotNull(message = "Patient SSN is required.")
    @Positive(message = "Patient SSN must be positive.")
    private Integer patientSsn;
    @NotNull(message = "Procedure code is required.")
    @Positive(message = "Procedure code must be positive.")
    private Integer procedureCode;
    @NotNull(message = "Stay ID is required.")
    @Positive(message = "Stay ID must be positive.")
    private Integer stayId;
    @NotNull(message = "Physician ID is required.")
    @Positive(message = "Physician ID must be positive.")
    private Integer physicianId;
    @NotNull(message = "Procedure date is required.")
    private java.time.LocalDateTime date;

    public UndergoesDTO() {}
    public UndergoesDTO(Integer patientSsn, Integer procedureCode, Integer stayId, Integer physicianId, java.time.LocalDateTime date) {
        this.patientSsn = patientSsn;
        this.procedureCode = procedureCode;
        this.stayId = stayId;
        this.physicianId = physicianId;
        this.date = date;
    }

    // Manual Accessors
    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }
    public Integer getProcedureCode() { return procedureCode; }
    public void setProcedureCode(Integer procedureCode) { this.procedureCode = procedureCode; }
    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }
    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }
    public java.time.LocalDateTime getDate() { return date; }
    public void setDate(java.time.LocalDateTime date) { this.date = date; }

    // Manual Builder
    public static class UndergoesDTOBuilder {
        private Integer patientSsn;
        private Integer procedureCode;
        private Integer stayId;
        private Integer physicianId;
        private java.time.LocalDateTime date;
        public UndergoesDTOBuilder patientSsn(Integer patientSsn) { this.patientSsn = patientSsn; return this; }
        public UndergoesDTOBuilder procedureCode(Integer procedureCode) { this.procedureCode = procedureCode; return this; }
        public UndergoesDTOBuilder stayId(Integer stayId) { this.stayId = stayId; return this; }
        public UndergoesDTOBuilder physicianId(Integer physicianId) { this.physicianId = physicianId; return this; }
        public UndergoesDTOBuilder date(java.time.LocalDateTime date) { this.date = date; return this; }
        public UndergoesDTO build() { return new UndergoesDTO(patientSsn, procedureCode, stayId, physicianId, date); }
    }
    public static UndergoesDTOBuilder builder() { return new UndergoesDTOBuilder(); }
}
