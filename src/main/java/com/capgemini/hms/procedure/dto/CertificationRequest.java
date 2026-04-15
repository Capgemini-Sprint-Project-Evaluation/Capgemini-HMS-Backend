package com.capgemini.hms.procedure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CertificationRequest {
    @NotNull
    @Schema(example = "101", description = "Employee ID of the physician being certified")
    private Integer physicianId;

    @NotNull
    @Schema(example = "1", description = "Code of the procedure the physician is trained in")
    private Integer procedureCode;

    @NotNull
    @Schema(example = "2026-01-01T00:00:00", description = "Date when the certification was issued")
    private LocalDateTime certificationDate;

    @NotNull
    @Schema(example = "2027-01-01T00:00:00", description = "Date when the certification expires")
    private LocalDateTime certificationExpires;

    public CertificationRequest() {}

    public CertificationRequest(Integer physicianId, Integer procedureCode, 
                                LocalDateTime certificationDate, LocalDateTime certificationExpires) {
        this.physicianId = physicianId;
        this.procedureCode = procedureCode;
        this.certificationDate = certificationDate;
        this.certificationExpires = certificationExpires;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getProcedureCode() { return procedureCode; }
    public void setProcedureCode(Integer procedureCode) { this.procedureCode = procedureCode; }

    public LocalDateTime getCertificationDate() { return certificationDate; }
    public void setCertificationDate(LocalDateTime certificationDate) { this.certificationDate = certificationDate; }

    public LocalDateTime getCertificationExpires() { return certificationExpires; }
    public void setCertificationExpires(LocalDateTime certificationExpires) { this.certificationExpires = certificationExpires; }
}
