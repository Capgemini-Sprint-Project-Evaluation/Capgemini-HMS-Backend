package com.capgemini.hms.scheduling.entity;

import com.capgemini.hms.patient.entity.Procedures;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "trained_in")
public class TrainedIn {

    @Id
    @EmbeddedId
    private TrainedInId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @ManyToOne
    @MapsId("procedure")
    @JoinColumn(name = "treatment", referencedColumnName = "code")
    private Procedures procedure;

    @Column(name = "certificationdate", nullable = false)
    private java.time.LocalDateTime treatmentdate;

    @Column(name = "certificationexpires", nullable = false)
    private java.time.LocalDateTime certificationexpires;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public TrainedIn() {}
    public TrainedIn(TrainedInId id, Physician physician, Procedures procedure, java.time.LocalDateTime treatmentdate, java.time.LocalDateTime certificationexpires, Boolean isDeleted) {
        this.id = id;
        this.physician = physician;
        this.procedure = procedure;
        this.treatmentdate = treatmentdate;
        this.certificationexpires = certificationexpires;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public TrainedInId getId() { return id; }
    public void setId(TrainedInId id) { this.id = id; }
    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }
    public Procedures getProcedure() { return procedure; }
    public void setProcedure(Procedures procedure) { this.procedure = procedure; }
    public java.time.LocalDateTime getTreatmentdate() { return treatmentdate; }
    public void setTreatmentdate(java.time.LocalDateTime treatmentdate) { this.treatmentdate = treatmentdate; }
    public java.time.LocalDateTime getCertificationexpires() { return certificationexpires; }
    public void setCertificationexpires(java.time.LocalDateTime certificationexpires) { this.certificationexpires = certificationexpires; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class TrainedInBuilder {
        private TrainedInId id;
        private Physician physician;
        private Procedures procedure;
        private java.time.LocalDateTime treatmentdate;
        private java.time.LocalDateTime certificationexpires;
        private Boolean isDeleted = false;
        public TrainedInBuilder id(TrainedInId id) { this.id = id; return this; }
        public TrainedInBuilder physician(Physician physician) { this.physician = physician; return this; }
        public TrainedInBuilder procedure(Procedures procedure) { this.procedure = procedure; return this; }
        public TrainedInBuilder treatmentdate(java.time.LocalDateTime treatmentdate) { this.treatmentdate = treatmentdate; return this; }
        public TrainedInBuilder certificationexpires(java.time.LocalDateTime certificationexpires) { this.certificationexpires = certificationexpires; return this; }
        public TrainedInBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public TrainedIn build() { return new TrainedIn(id, physician, procedure, treatmentdate, certificationexpires, isDeleted); }
    }
    public static TrainedInBuilder builder() { return new TrainedInBuilder(); }
}

// TrainedInId moved to external file
