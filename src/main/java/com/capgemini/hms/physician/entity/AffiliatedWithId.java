package com.capgemini.hms.physician.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AffiliatedWithId implements Serializable {
    @Column(name = "physician")
    private Integer physician;

    @Column(name = "department")
    private Integer department;

    public AffiliatedWithId() {
    }

    public AffiliatedWithId(Integer physician, Integer department) {
        this.physician = physician;
        this.department = department;
    }

    public Integer getPhysician() {
        return physician;
    }

    public void setPhysician(Integer physician) {
        this.physician = physician;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffiliatedWithId that = (AffiliatedWithId) o;
        return Objects.equals(physician, that.physician) && 
               Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(physician, department);
    }
}
