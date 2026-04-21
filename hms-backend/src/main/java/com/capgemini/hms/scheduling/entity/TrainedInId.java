package com.capgemini.hms.scheduling.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainedInId implements Serializable {
    @Column(name = "physician")
    private Integer physician;

    @Column(name = "treatment")
    private Integer procedure;
}
