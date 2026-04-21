package com.capgemini.hms.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @Column(name = "departmentid")
    private Integer departmentId;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Department name is required.")
    @Size(max = 255, message = "Department name must not exceed 255 characters.")
    private String name;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "head")
    private Integer headId;

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public Integer getHeadId() { return headId; }
    public void setHeadId(Integer headId) { this.headId = headId; }
}
