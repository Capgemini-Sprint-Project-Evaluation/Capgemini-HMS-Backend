package com.capgemini.hms.clinical.dto;

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
public class MedicationDTO {
    @NotNull(message = "Medication code is required")
    @Positive(message = "Medication code must be positive.")
    private Integer code;

    @NotBlank(message = "Medication name is required")
    @Size(max = 255, message = "Medication name must not exceed 255 characters.")
    private String name;

    @NotBlank(message = "Brand name is required")
    @Size(max = 255, message = "Brand name must not exceed 255 characters.")
    private String brand;

    @NotBlank(message = "Description is required")
    private String description;


}
