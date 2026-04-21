package com.capgemini.hms.scheduling.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class TrainedInDTO {
    @NotNull(message = "Physician ID is required.")
    @Positive(message = "Physician ID must be positive.")
    private Integer physicianId;
    private String physicianName;
    @NotNull(message = "Procedure code is required.")
    @Positive(message = "Procedure code must be positive.")
    private Integer procedureCode;
    private String procedureName;
    @NotNull(message = "Treatment date is required.")
    private java.time.LocalDateTime treatmentDate;
    @NotNull(message = "Certification expiry is required.")
    private java.time.LocalDateTime certificationExpires;

    public TrainedInDTO() {}
    public TrainedInDTO(Integer physicianId, String physicianName, Integer procedureCode, String procedureName, java.time.LocalDateTime treatmentDate, java.time.LocalDateTime certificationExpires) {
        this.physicianId = physicianId;
        this.physicianName = physicianName;
        this.procedureCode = procedureCode;
        this.procedureName = procedureName;
        this.treatmentDate = treatmentDate;
        this.certificationExpires = certificationExpires;
    }

    // Manual Accessors
    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }
    public String getPhysicianName() { return physicianName; }
    public void setPhysicianName(String physicianName) { this.physicianName = physicianName; }
    public Integer getProcedureCode() { return procedureCode; }
    public void setProcedureCode(Integer procedureCode) { this.procedureCode = procedureCode; }
    public String getProcedureName() { return procedureName; }
    public void setProcedureName(String procedureName) { this.procedureName = procedureName; }
    public java.time.LocalDateTime getTreatmentDate() { return treatmentDate; }
    public void setTreatmentDate(java.time.LocalDateTime treatmentDate) { this.treatmentDate = treatmentDate; }
    public java.time.LocalDateTime getCertificationExpires() { return certificationExpires; }
    public void setCertificationExpires(java.time.LocalDateTime certificationExpires) { this.certificationExpires = certificationExpires; }

    // Manual Builder
    public static class TrainedInDTOBuilder {
        private Integer physicianId;
        private String physicianName;
        private Integer procedureCode;
        private String procedureName;
        private java.time.LocalDateTime treatmentDate;
        private java.time.LocalDateTime certificationExpires;
        public TrainedInDTOBuilder physicianId(Integer physicianId) { this.physicianId = physicianId; return this; }
        public TrainedInDTOBuilder physicianName(String physicianName) { this.physicianName = physicianName; return this; }
        public TrainedInDTOBuilder procedureCode(Integer procedureCode) { this.procedureCode = procedureCode; return this; }
        public TrainedInDTOBuilder procedureName(String procedureName) { this.procedureName = procedureName; return this; }
        public TrainedInDTOBuilder treatmentDate(java.time.LocalDateTime treatmentDate) { this.treatmentDate = treatmentDate; return this; }
        public TrainedInDTOBuilder certificationExpires(java.time.LocalDateTime certificationExpires) { this.certificationExpires = certificationExpires; return this; }
        public TrainedInDTO build() { return new TrainedInDTO(physicianId, physicianName, procedureCode, procedureName, treatmentDate, certificationExpires); }
    }
    public static TrainedInDTOBuilder builder() { return new TrainedInDTOBuilder(); }
}
