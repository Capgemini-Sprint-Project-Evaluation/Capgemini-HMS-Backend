package com.capgemini.hms.auth.entity;
import jakarta.persistence.Column;
import lombok.*;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliatedWithId implements Serializable {
    @Column(name = "physician")
    private Integer physician;

    @Column(name = "department")
    private Integer department;


}
