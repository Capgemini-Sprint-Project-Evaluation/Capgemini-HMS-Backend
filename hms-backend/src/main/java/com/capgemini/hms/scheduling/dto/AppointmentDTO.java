package com.capgemini.hms.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Integer appointmentId;
    @NotNull(message = "Patient SSN is required.")
    @Positive(message = "Patient SSN must be positive.")
    private Integer patientSsn;
    @NotNull(message = "Physician ID is required.")
    @Positive(message = "Physician ID must be positive.")
    private Integer physicianId;
    private Integer prepNurseId;
    @NotNull(message = "Appointment start time is required.")
    private LocalDateTime start;
    @NotNull(message = "Appointment end time is required.")
    private LocalDateTime end;
    @NotBlank(message = "Examination room is required.")
    private String examinationRoom;

    public AppointmentDTO() {}
    public AppointmentDTO(Integer appointmentId, Integer patientSsn, Integer physicianId, Integer prepNurseId, LocalDateTime start, LocalDateTime end, String examinationRoom) {
        this.appointmentId = appointmentId;
        this.patientSsn = patientSsn;
        this.physicianId = physicianId;
        this.prepNurseId = prepNurseId;
        this.start = start;
        this.end = end;
        this.examinationRoom = examinationRoom;
    }

    // Manual Accessors
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }
    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }
    public Integer getPrepNurseId() { return prepNurseId; }
    public void setPrepNurseId(Integer prepNurseId) { this.prepNurseId = prepNurseId; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public String getExaminationRoom() { return examinationRoom; }
    public void setExaminationRoom(String examinationRoom) { this.examinationRoom = examinationRoom; }

    // Manual Builder
    public static class AppointmentDTOBuilder {
        private Integer appointmentId;
        private Integer patientSsn;
        private Integer physicianId;
        private Integer prepNurseId;
        private LocalDateTime start;
        private LocalDateTime end;
        private String examinationRoom;
        public AppointmentDTOBuilder appointmentId(Integer appointmentId) { this.appointmentId = appointmentId; return this; }
        public AppointmentDTOBuilder patientSsn(Integer patientSsn) { this.patientSsn = patientSsn; return this; }
        public AppointmentDTOBuilder physicianId(Integer physicianId) { this.physicianId = physicianId; return this; }
        public AppointmentDTOBuilder prepNurseId(Integer prepNurseId) { this.prepNurseId = prepNurseId; return this; }
        public AppointmentDTOBuilder start(LocalDateTime start) { this.start = start; return this; }
        public AppointmentDTOBuilder end(LocalDateTime end) { this.end = end; return this; }
        public AppointmentDTOBuilder examinationRoom(String examinationRoom) { this.examinationRoom = examinationRoom; return this; }
        public AppointmentDTO build() { return new AppointmentDTO(appointmentId, patientSsn, physicianId, prepNurseId, start, end, examinationRoom); }
    }
    public static AppointmentDTOBuilder builder() { return new AppointmentDTOBuilder(); }
}
