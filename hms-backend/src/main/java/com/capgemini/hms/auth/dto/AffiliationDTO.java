package com.capgemini.hms.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliationDTO {
    @NotNull(message = "Physician ID is required.")
    private Integer physicianId;
    private String physicianName;
    @NotNull(message = "Department ID is required.")
    private Integer departmentId;
    private String departmentName;
    @NotNull(message = "Primary affiliation flag is required.")
    private Boolean primaryAffiliation;


}
