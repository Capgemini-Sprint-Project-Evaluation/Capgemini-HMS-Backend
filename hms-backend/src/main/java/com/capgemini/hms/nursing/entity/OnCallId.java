package com.capgemini.hms.nursing.entity;

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
public class OnCallId implements Serializable {
    private Integer nurse;
    
    @Column(name = "blockfloor")
    private Integer blockFloor;
    
    @Column(name = "blockcode")
    private Integer blockCode;
    
    @Column(name = "oncallstart")
    private java.time.LocalDateTime onCallStart;
    
    @Column(name = "oncallend")
    private java.time.LocalDateTime onCallEnd;


}
