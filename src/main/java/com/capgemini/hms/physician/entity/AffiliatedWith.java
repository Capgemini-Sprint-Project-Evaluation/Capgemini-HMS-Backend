package com.capgemini.hms.physician.entity;

import com.capgemini.hms.department.entity.Department;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "affiliated_with")
public class AffiliatedWith {

    @EmbeddedId
    private AffiliatedWithId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician", referencedColumnName = "employeeid")
    private Physician physician;

    @ManyToOne
    @MapsId("department")
    @JoinColumn(name = "department", referencedColumnName = "departmentid")
    private Department department;

    @Column(name = "primaryaffiliation", nullable = false)
    private Boolean primaryAffiliation;

    public AffiliatedWith() {
    }

    public AffiliatedWith(AffiliatedWithId id, Physician physician, Department department, Boolean primaryAffiliation) {
        this.id = id;
        this.physician = physician;
        this.department = department;
        this.primaryAffiliation = primaryAffiliation;
    }

    public AffiliatedWithId getId() {
        return id;
    }

    public void setId(AffiliatedWithId id) {
        this.id = id;
    }

    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getPrimaryAffiliation() {
        return primaryAffiliation;
    }

    public void setPrimaryAffiliation(Boolean primaryAffiliation) {
        this.primaryAffiliation = primaryAffiliation;
    }
}
