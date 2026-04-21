package com.capgemini.hms.patient.entity;

import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UndergoesId implements Serializable {
    @Column(name = "patient")
    private Integer patient;

    @Column(name = "procedure")
    private Integer procedure;

    @Column(name = "stay")
    private Integer stay;

    @Column(name = "dateundergoes")
    private java.time.LocalDateTime dateundergoes;
}
