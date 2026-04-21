package com.capgemini.hms.clinical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {
    @NotNull(message = "Physician ID is required.")
    @Positive(message = "Physician ID must be positive.")
    private Integer physicianId;
    @NotNull(message = "Patient SSN is required.")
    @Positive(message = "Patient SSN must be positive.")
    private Integer patientSsn;
    @NotNull(message = "Medication code is required.")
    @Positive(message = "Medication code must be positive.")
    private Integer medicationCode;
    @NotNull(message = "Prescription date is required.")
    private LocalDateTime date;
    @NotBlank(message = "Dose is required.")
    private String dose;
    private Integer appointmentId;


}
