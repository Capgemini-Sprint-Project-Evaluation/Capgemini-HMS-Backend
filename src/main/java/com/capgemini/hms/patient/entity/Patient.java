package com.capgemini.hms.patient.entity;

import com.capgemini.hms.physician.entity.Physician;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @Column(name = "ssn")
    private Integer ssn;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "insuranceid", nullable = false)
    private String insuranceId;

    @ManyToOne
    @JoinColumn(name = "pcp", referencedColumnName = "employeeid")
    private Physician pcp;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Patient() {
    }

    public Patient(Integer ssn, String name, String address, String phone, String insuranceId, Physician pcp) {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.pcp = pcp;
    }

    public Integer getSsn() {
        return ssn;
    }

    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Physician getPcp() {
        return pcp;
    }

    public void setPcp(Physician pcp) {
        this.pcp = pcp;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
