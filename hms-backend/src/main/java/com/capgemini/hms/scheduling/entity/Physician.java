package com.capgemini.hms.scheduling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "physician")
public class Physician {

    @Id
    @Column(name = "employeeid")
    private Integer employeeId;

    private String name;
    private String position;
    private Integer ssn;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public Physician() {}

    public Physician(Integer employeeId, String name, String position, Integer ssn, Boolean isDeleted) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.ssn = ssn;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    // Manual Accessors
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    // Manual Builder
    public static class PhysicianBuilder {
        private Integer employeeId;
        private String name;
        private String position;
        private Integer ssn;
        private Boolean isDeleted = false;

        public PhysicianBuilder employeeId(Integer employeeId) { this.employeeId = employeeId; return this; }
        public PhysicianBuilder name(String name) { this.name = name; return this; }
        public PhysicianBuilder position(String position) { this.position = position; return this; }
        public PhysicianBuilder ssn(Integer ssn) { this.ssn = ssn; return this; }
        public PhysicianBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public Physician build() {
            return new Physician(employeeId, name, position, ssn, isDeleted);
        }
    }
    public static PhysicianBuilder builder() { return new PhysicianBuilder(); }
}
