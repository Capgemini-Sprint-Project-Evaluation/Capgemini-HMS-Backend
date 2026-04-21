package com.capgemini.hms.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @Column(name = "roomnumber")
    private Integer roomNumber;

    @Column(name = "roomtype", nullable = false, length = 255)
    private String roomType;

    @ManyToOne
    @JoinColumn(name = "blockfloor", referencedColumnName = "blockfloor")
    @JoinColumn(name = "blockcode", referencedColumnName = "blockcode")
    private Block block;

    @Column(nullable = false)
    private Boolean unavailable;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


}
