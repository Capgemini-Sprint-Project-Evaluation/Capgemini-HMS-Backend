package com.capgemini.hms.patient.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class StayDTO {
    private Integer stayId;
    @NotNull(message = "Patient SSN is required.")
    @Positive(message = "Patient SSN must be positive.")
    private Integer patientSsn;
    @NotNull(message = "Room number is required.")
    @Positive(message = "Room number must be positive.")
    private Integer roomNumber;
    @NotNull(message = "Stay start time is required.")
    private java.time.LocalDateTime startTime;
    private java.time.LocalDateTime endTime;

    public StayDTO() {}
    public StayDTO(Integer stayId, Integer patientSsn, Integer roomNumber, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        this.stayId = stayId;
        this.patientSsn = patientSsn;
        this.roomNumber = roomNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Manual Accessors
    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }
    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }
    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }
    public java.time.LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(java.time.LocalDateTime startTime) { this.startTime = startTime; }
    public java.time.LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(java.time.LocalDateTime endTime) { this.endTime = endTime; }

    // Manual Builder
    public static class StayDTOBuilder {
        private Integer stayId;
        private Integer patientSsn;
        private Integer roomNumber;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;
        public StayDTOBuilder stayId(Integer stayId) { this.stayId = stayId; return this; }
        public StayDTOBuilder patientSsn(Integer patientSsn) { this.patientSsn = patientSsn; return this; }
        public StayDTOBuilder roomNumber(Integer roomNumber) { this.roomNumber = roomNumber; return this; }
        public StayDTOBuilder startTime(java.time.LocalDateTime startTime) { this.startTime = startTime; return this; }
        public StayDTOBuilder endTime(java.time.LocalDateTime endTime) { this.endTime = endTime; return this; }
        public StayDTO build() { return new StayDTO(stayId, patientSsn, roomNumber, startTime, endTime); }
    }
    public static StayDTOBuilder builder() { return new StayDTOBuilder(); }
}
