package com.capgemini.hms.prescription.entity;

import com.capgemini.hms.appointment.entity.Appointment;
import com.capgemini.hms.medication.entity.Medication;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.physician.entity.Physician;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * Prescription entity represents the association between a Physician, Patient, and Medication.
 * It maps to the 'prescribes' table in the database and tracks medication doses and appointment links.
 */
@Entity
@Table(name = "prescribes")
public class Prescription {

    @EmbeddedId
    private PrescriptionId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician; // The doctor who prescribed the medication

    @ManyToOne
    @MapsId("patient")
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @MapsId("medication")
    @JoinColumn(name = "medication", referencedColumnName = "code")
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "appointment", referencedColumnName = "appointmentid")
    private Appointment appointment;

    @Column(name = "dose", nullable = false)
    private String dose;

    public Prescription() {
    }

    public Prescription(PrescriptionId id, Physician physician, Patient patient, Medication medication, Appointment appointment, String dose) {
        this.id = id;
        this.physician = physician;
        this.patient = patient;
        this.medication = medication;
        this.appointment = appointment;
        this.dose = dose;
    }

    public PrescriptionId getId() {
        return id;
    }

    public void setId(PrescriptionId id) {
        this.id = id;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
