package com.capgemini.hms.clinical.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    private Integer code;

    private String name;
    private String brand;
    private String description;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
