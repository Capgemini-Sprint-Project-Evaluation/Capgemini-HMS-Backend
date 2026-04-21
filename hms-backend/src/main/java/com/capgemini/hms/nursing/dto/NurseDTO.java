package com.capgemini.hms.nursing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurseDTO {
    @NotNull(message = "Employee ID is required.")
    @Positive(message = "Employee ID must be positive.")
    private Integer employeeId;
    @NotBlank(message = "Nurse name is required.")
    @Size(max = 255, message = "Nurse name must not exceed 255 characters.")
    private String name;
    @NotBlank(message = "Position is required.")
    private String position;
    @NotNull(message = "Registration status is required.")
    private Boolean registered;
    @NotNull(message = "SSN is required.")
    @Positive(message = "SSN must be positive.")
    private Integer ssn;


}
