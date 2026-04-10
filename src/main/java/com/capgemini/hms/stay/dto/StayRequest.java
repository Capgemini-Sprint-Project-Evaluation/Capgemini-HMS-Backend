package com.capgemini.hms.stay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class StayRequest {
    @NotNull
    @Schema(example = "100000001", description = "SSN of the patient being admitted")
    private Integer patientSsn;

    @NotNull
    @Schema(example = "123", description = "Number of the room assigned to the patient")
    private Integer roomNumber;

    @NotNull
    @Schema(example = "2026-04-10T10:00:00", description = "Admission date and time")
    private LocalDateTime stayStart;

    @Schema(example = "2026-04-15T10:00:00", description = "Expected discharge date and time")
    private LocalDateTime stayEnd;

    @Schema(example = "Patient requires observation for post-op recovery.", description = "Clinical notes for the admission")
    private String notes;

    public StayRequest() {}

    public StayRequest(Integer patientSsn, Integer roomNumber, LocalDateTime stayStart, LocalDateTime stayEnd, String notes) {
        this.patientSsn = patientSsn;
        this.roomNumber = roomNumber;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
        this.notes = notes;
    }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getStayStart() { return stayStart; }
    public void setStayStart(LocalDateTime stayStart) { this.stayStart = stayStart; }

    public LocalDateTime getStayEnd() { return stayEnd; }
    public void setStayEnd(LocalDateTime stayEnd) { this.stayEnd = stayEnd; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
