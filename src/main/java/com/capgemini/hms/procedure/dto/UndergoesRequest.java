package com.capgemini.hms.procedure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UndergoesRequest {
    @NotNull
    @Schema(example = "100000001", description = "SSN of the patient undergoing the procedure")
    private Integer patientSsn;

    @NotNull
    @Schema(example = "1", description = "Unique code of the procedure being performed")
    private Integer procedureCode;

    @NotNull
    @Schema(example = "1', description = 'Unique ID of the patient stay (admission record)")
    private Integer stayId;

    @NotNull
    @Schema(example = "2026-04-10T11:00:00", description = "Timestamp when the procedure was performed")
    private LocalDateTime dateUndergoes;

    @NotNull
    @Schema(example = "101", description = "Employee ID of the physician performing the procedure")
    private Integer physicianId;

    @NotNull
    @Schema(example = "501", description = "Employee ID of the nurse assisting the procedure")
    private Integer assistingNurseId;

    @Schema(example = "Procedure successful. Patient stable.", description = "Clinical procedural notes")
    private String notes;

    public UndergoesRequest() {}

    public UndergoesRequest(Integer patientSsn, Integer procedureCode, Integer stayId, 
                            LocalDateTime dateUndergoes, Integer physicianId, Integer assistingNurseId, String notes) {
        this.patientSsn = patientSsn;
        this.procedureCode = procedureCode;
        this.stayId = stayId;
        this.dateUndergoes = dateUndergoes;
        this.physicianId = physicianId;
        this.assistingNurseId = assistingNurseId;
        this.notes = notes;
    }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getProcedureCode() { return procedureCode; }
    public void setProcedureCode(Integer procedureCode) { this.procedureCode = procedureCode; }

    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }

    public LocalDateTime getDateUndergoes() { return dateUndergoes; }
    public void setDateUndergoes(LocalDateTime dateUndergoes) { this.dateUndergoes = dateUndergoes; }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getAssistingNurseId() { return assistingNurseId; }
    public void setAssistingNurseId(Integer assistingNurseId) { this.assistingNurseId = assistingNurseId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
