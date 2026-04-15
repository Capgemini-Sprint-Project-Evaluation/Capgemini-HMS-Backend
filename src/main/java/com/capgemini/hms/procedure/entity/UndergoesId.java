package com.capgemini.hms.procedure.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class UndergoesId implements Serializable {
    private Integer patient;

    @jakarta.persistence.Column(name = "`procedure`")
    private Integer procedure;
    private Integer stay;
    private LocalDateTime dateUndergoes;

    public UndergoesId() {
    }

    public UndergoesId(Integer patient, Integer procedure, Integer stay, LocalDateTime dateUndergoes) {
        this.patient = patient;
        this.procedure = procedure;
        this.stay = stay;
        this.dateUndergoes = dateUndergoes;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }

    public Integer getProcedure() {
        return procedure;
    }

    public void setProcedure(Integer procedure) {
        this.procedure = procedure;
    }

    public Integer getStay() {
        return stay;
    }

    public void setStay(Integer stay) {
        this.stay = stay;
    }

    public LocalDateTime getDateUndergoes() {
        return dateUndergoes;
    }

    public void setDateUndergoes(LocalDateTime dateUndergoes) {
        this.dateUndergoes = dateUndergoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UndergoesId that = (UndergoesId) o;
        return Objects.equals(patient, that.patient) && 
               Objects.equals(procedure, that.procedure) && 
               Objects.equals(stay, that.stay) && 
               Objects.equals(dateUndergoes, that.dateUndergoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, procedure, stay, dateUndergoes);
    }
}
