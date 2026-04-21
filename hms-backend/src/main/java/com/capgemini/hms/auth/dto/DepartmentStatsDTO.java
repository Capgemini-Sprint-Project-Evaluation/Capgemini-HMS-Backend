package com.capgemini.hms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentStatsDTO {
    private Integer departmentId;
    private String departmentName;
    private long physicianCount;


}
