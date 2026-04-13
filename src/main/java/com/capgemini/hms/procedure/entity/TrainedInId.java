package com.capgemini.hms.procedure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TrainedInId implements Serializable {
    @Column(name = "physician")
    private Integer physician;

    @Column(name = "treatment")
    private Integer treatment;

    public TrainedInId() {
    }

    public TrainedInId(Integer physician, Integer treatment) {
        this.physician = physician;
        this.treatment = treatment;
    }

    public Integer getPhysician() {
        return physician;
    }

    public void setPhysician(Integer physician) {
        this.physician = physician;
    }

    public Integer getTreatment() {
        return treatment;
    }

    public void setTreatment(Integer treatment) {
        this.treatment = treatment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainedInId that = (TrainedInId) o;
        return Objects.equals(physician, that.physician) && 
               Objects.equals(treatment, that.treatment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physician, treatment);
    }
}
