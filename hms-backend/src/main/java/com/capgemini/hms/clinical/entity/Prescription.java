package com.capgemini.hms.clinical.entity;

import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prescribes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @EmbeddedId
    private PrescriptionId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @ManyToOne
    @MapsId("patient")
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @MapsId("medication")
    @JoinColumn(name = "medication", referencedColumnName = "code")
    private Medication medication;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dose;
    
    @Column(name = "appointment")
    private Integer appointmentId;


}
