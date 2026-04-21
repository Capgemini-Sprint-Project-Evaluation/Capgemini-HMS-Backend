package com.capgemini.hms.patient.entity;

import com.capgemini.hms.scheduling.entity.Physician;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "undergoes")
public class Undergoes {

    @Id
    @EmbeddedId
    private UndergoesId id;

    @ManyToOne
    @MapsId("patient")
    @JoinColumn(name = "patient", referencedColumnName = "ssn")
    private Patient patient;

    @ManyToOne
    @MapsId("procedure")
    @JoinColumn(name = "procedure", referencedColumnName = "code")
    private Procedures procedure;

    @ManyToOne
    @MapsId("stay")
    @JoinColumn(name = "stay", referencedColumnName = "stayid")
    private Stay stay;

    @ManyToOne
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Undergoes() {}
    public Undergoes(UndergoesId id, Patient patient, Procedures procedure, Stay stay, Physician physician, Boolean isDeleted) {
        this.id = id;
        this.patient = patient;
        this.procedure = procedure;
        this.stay = stay;
        this.physician = physician;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public UndergoesId getId() { return id; }
    public void setId(UndergoesId id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Procedures getProcedure() { return procedure; }
    public void setProcedure(Procedures procedure) { this.procedure = procedure; }
    public Stay getStay() { return stay; }
    public void setStay(Stay stay) { this.stay = stay; }
    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class UndergoesBuilder {
        private UndergoesId id;
        private Patient patient;
        private Procedures procedure;
        private Stay stay;
        private Physician physician;
        private Boolean isDeleted = false;
        public UndergoesBuilder id(UndergoesId id) { this.id = id; return this; }
        public UndergoesBuilder patient(Patient patient) { this.patient = patient; return this; }
        public UndergoesBuilder procedure(Procedures procedure) { this.procedure = procedure; return this; }
        public UndergoesBuilder stay(Stay stay) { this.stay = stay; return this; }
        public UndergoesBuilder physician(Physician physician) { this.physician = physician; return this; }
        public UndergoesBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Undergoes build() { return new Undergoes(id, patient, procedure, stay, physician, isDeleted); }
    }
    public static UndergoesBuilder builder() { return new UndergoesBuilder(); }
}

// UndergoesId moved to external file
