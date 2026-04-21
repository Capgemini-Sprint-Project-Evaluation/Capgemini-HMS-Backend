package com.capgemini.hms.nursing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nurse")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nurse {
    @Id
    @Column(name = "employeeid")
    private Integer employeeId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String position;

    @Column(nullable = false)
    private Boolean registered;

    @Column(nullable = false)
    private Integer ssn;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


}
