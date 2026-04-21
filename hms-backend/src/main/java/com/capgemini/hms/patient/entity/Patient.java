package com.capgemini.hms.patient.entity;

import com.capgemini.hms.scheduling.entity.Physician;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    private Integer ssn;

    private String name;
    private String address;
    private String phone;

    @Column(name = "insuranceid")
    private String insuranceId;

    @ManyToOne
    @JoinColumn(name = "pcp", referencedColumnName = "employeeid")
    private Physician pcp;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Patient() {}
    public Patient(Integer ssn, String name, String address, String phone, String insuranceId, Physician pcp, Boolean isDeleted) {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.pcp = pcp;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getInsuranceId() { return insuranceId; }
    public void setInsuranceId(String insuranceId) { this.insuranceId = insuranceId; }
    public Physician getPcp() { return pcp; }
    public void setPcp(Physician pcp) { this.pcp = pcp; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class PatientBuilder {
        private Integer ssn;
        private String name;
        private String address;
        private String phone;
        private String insuranceId;
        private Physician pcp;
        private Boolean isDeleted = false;
        public PatientBuilder ssn(Integer ssn) { this.ssn = ssn; return this; }
        public PatientBuilder name(String name) { this.name = name; return this; }
        public PatientBuilder address(String address) { this.address = address; return this; }
        public PatientBuilder phone(String phone) { this.phone = phone; return this; }
        public PatientBuilder insuranceId(String insuranceId) { this.insuranceId = insuranceId; return this; }
        public PatientBuilder pcp(Physician pcp) { this.pcp = pcp; return this; }
        public PatientBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Patient build() { return new Patient(ssn, name, address, phone, insuranceId, pcp, isDeleted); }
    }
    public static PatientBuilder builder() { return new PatientBuilder(); }
}
