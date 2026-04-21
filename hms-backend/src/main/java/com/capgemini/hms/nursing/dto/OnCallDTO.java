package com.capgemini.hms.nursing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnCallDTO {
    private Integer nurseId;
    private String nurseName;
    private Integer blockFloor;
    private Integer blockCode;
    private java.time.LocalDateTime onCallStart;
    private java.time.LocalDateTime onCallEnd;


}
