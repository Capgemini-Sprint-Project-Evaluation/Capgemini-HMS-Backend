package com.capgemini.hms.stay.entity;

import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "stay")
public class Stay {

    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "stayid")
    private Integer stayId;

    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "roomnumber")
    private Room room;

    @Column(name = "staystart", nullable = false)
    private LocalDateTime stayStart;

    @Column(name = "stayend", nullable = true)
    private LocalDateTime stayEnd;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Stay() {
    }

    public Stay(Integer stayId, Patient patient, Room room, LocalDateTime stayStart, LocalDateTime stayEnd, String notes) {
        this.stayId = stayId;
        this.patient = patient;
        this.room = room;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
        this.notes = notes;
    }

    public Integer getStayId() {
        return stayId;
    }

    public void setStayId(Integer stayId) {
        this.stayId = stayId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStayStart() {
        return stayStart;
    }

    public void setStayStart(LocalDateTime stayStart) {
        this.stayStart = stayStart;
    }

    public LocalDateTime getStayEnd() {
        return stayEnd;
    }

    public void setStayEnd(LocalDateTime stayEnd) {
        this.stayEnd = stayEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
