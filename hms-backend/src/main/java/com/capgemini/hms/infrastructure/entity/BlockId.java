package com.capgemini.hms.infrastructure.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockId implements Serializable {
    private Integer blockfloor;
    private Integer blockcode;


}
