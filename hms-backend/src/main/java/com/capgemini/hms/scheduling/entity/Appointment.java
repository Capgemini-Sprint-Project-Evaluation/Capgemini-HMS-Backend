package com.capgemini.hms.scheduling.entity;

import com.capgemini.hms.nursing.entity.Nurse;
import com.capgemini.hms.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @Column(name = "appointmentid")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "prepnurse", referencedColumnName = "employeeid")
    private Nurse prepNurse;

    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    @Column(name = "\"end\"", nullable = false)
    private LocalDateTime end;

    @Column(name = "examinationroom", nullable = false)
    private String examinationRoom;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Appointment() {}
    public Appointment(Integer appointmentId, Patient patient, Physician physician, Nurse prepNurse, LocalDateTime start, LocalDateTime end, String examinationRoom, Boolean isDeleted) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.physician = physician;
        this.prepNurse = prepNurse;
        this.start = start;
        this.end = end;
        this.examinationRoom = examinationRoom;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }
    public Nurse getPrepNurse() { return prepNurse; }
    public void setPrepNurse(Nurse prepNurse) { this.prepNurse = prepNurse; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public String getExaminationRoom() { return examinationRoom; }
    public void setExaminationRoom(String examinationRoom) { this.examinationRoom = examinationRoom; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class AppointmentBuilder {
        private Integer appointmentId;
        private Patient patient;
        private Physician physician;
        private Nurse prepNurse;
        private LocalDateTime start;
        private LocalDateTime end;
        private String examinationRoom;
        private Boolean isDeleted = false;
        public AppointmentBuilder appointmentId(Integer appointmentId) { this.appointmentId = appointmentId; return this; }
        public AppointmentBuilder patient(Patient patient) { this.patient = patient; return this; }
        public AppointmentBuilder physician(Physician physician) { this.physician = physician; return this; }
        public AppointmentBuilder prepNurse(Nurse prepNurse) { this.prepNurse = prepNurse; return this; }
        public AppointmentBuilder start(LocalDateTime start) { this.start = start; return this; }
        public AppointmentBuilder end(LocalDateTime end) { this.end = end; return this; }
        public AppointmentBuilder examinationRoom(String examinationRoom) { this.examinationRoom = examinationRoom; return this; }
        public AppointmentBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Appointment build() { return new Appointment(appointmentId, patient, physician, prepNurse, start, end, examinationRoom, isDeleted); }
    }
    public static AppointmentBuilder builder() { return new AppointmentBuilder(); }
}
