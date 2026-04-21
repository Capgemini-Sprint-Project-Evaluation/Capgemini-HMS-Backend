package com.capgemini.hms.patient.entity;

import com.capgemini.hms.infrastructure.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stay")
public class Stay {
    @Id
    @Column(name = "stayid")
    private Integer stayId;

    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "roomnumber")
    private Room room;

    @Column(name = "staystart", nullable = false)
    private java.time.LocalDateTime stayStart;

    @Column(name = "stayend")
    private java.time.LocalDateTime stayEnd;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Stay() {}
    public Stay(Integer stayId, Patient patient, Room room, java.time.LocalDateTime stayStart, java.time.LocalDateTime stayEnd, String notes, Boolean isDeleted) {
        this.stayId = stayId;
        this.patient = patient;
        this.room = room;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
        this.notes = notes;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public java.time.LocalDateTime getStayStart() { return stayStart; }
    public void setStayStart(java.time.LocalDateTime stayStart) { this.stayStart = stayStart; }
    public java.time.LocalDateTime getStayEnd() { return stayEnd; }
    public void setStayEnd(java.time.LocalDateTime stayEnd) { this.stayEnd = stayEnd; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class StayBuilder {
        private Integer stayId;
        private Patient patient;
        private Room room;
        private java.time.LocalDateTime stayStart;
        private java.time.LocalDateTime stayEnd;
        private String notes;
        private Boolean isDeleted = false;
        public StayBuilder stayId(Integer stayId) { this.stayId = stayId; return this; }
        public StayBuilder patient(Patient patient) { this.patient = patient; return this; }
        public StayBuilder room(Room room) { this.room = room; return this; }
        public StayBuilder stayStart(java.time.LocalDateTime stayStart) { this.stayStart = stayStart; return this; }
        public StayBuilder stayEnd(java.time.LocalDateTime stayEnd) { this.stayEnd = stayEnd; return this; }
        public StayBuilder notes(String notes) { this.notes = notes; return this; }
        public StayBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Stay build() { return new Stay(stayId, patient, room, stayStart, stayEnd, notes, isDeleted); }
    }
    public static StayBuilder builder() { return new StayBuilder(); }
}
