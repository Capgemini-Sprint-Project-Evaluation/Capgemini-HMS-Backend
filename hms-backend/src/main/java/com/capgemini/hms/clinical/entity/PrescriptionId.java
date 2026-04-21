package com.capgemini.hms.clinical.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionId implements Serializable {
    private Integer physician;
    private Integer patient;
    private Integer medication;
    
    @Column(name = "date")
    private LocalDateTime date;



    // Equals/HashCode recommended for @Embeddable but skipping for speed unless build fails on them
}
