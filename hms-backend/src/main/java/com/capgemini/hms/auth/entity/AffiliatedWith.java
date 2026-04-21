package com.capgemini.hms.auth.entity;

import com.capgemini.hms.scheduling.entity.Physician;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "affiliated_with")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(nullable = false)
    private Boolean primaryaffiliation;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    public AffiliatedWithId getId() { return id; }
    public void setId(AffiliatedWithId id) { this.id = id; }
    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Boolean getPrimaryaffiliation() { return primaryaffiliation; }
    public void setPrimaryaffiliation(Boolean primaryaffiliation) { this.primaryaffiliation = primaryaffiliation; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}

