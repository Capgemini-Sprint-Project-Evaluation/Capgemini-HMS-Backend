package com.capgemini.hms.stay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class StayResponse {
    @Schema(example = "1", description = "Unique ID of the stay record")
    private Integer stayId;

    @Schema(example = "John Smith", description = "Name of the admitted patient")
    private String patientName;

    @Schema(example = "123", description = "Number of the assigned room")
    private Integer roomNumber;

    @Schema(example = "2026-04-10T10:00:00", description = "Admission timestamp")
    private LocalDateTime stayStart;

    @Schema(example = "2026-04-15T10:00:00", description = "Discharge timestamp")
    private LocalDateTime stayEnd;

    @Schema(example = "Patient recovered well.", description = "Clinical observation notes")
    private String notes;

    public StayResponse() {}

    public StayResponse(Integer stayId, String patientName, Integer roomNumber, 
                        LocalDateTime stayStart, LocalDateTime stayEnd, String notes) {
        this.stayId = stayId;
        this.patientName = patientName;
        this.roomNumber = roomNumber;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
        this.notes = notes;
    }

    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getStayStart() { return stayStart; }
    public void setStayStart(LocalDateTime stayStart) { this.stayStart = stayStart; }

    public LocalDateTime getStayEnd() { return stayEnd; }
    public void setStayEnd(LocalDateTime stayEnd) { this.stayEnd = stayEnd; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
