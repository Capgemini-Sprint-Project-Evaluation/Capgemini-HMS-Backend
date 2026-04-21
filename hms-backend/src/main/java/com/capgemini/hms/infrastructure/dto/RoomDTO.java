package com.capgemini.hms.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    @NotNull(message = "Room number is required.")
    @Positive(message = "Room number must be positive.")
    private Integer roomNumber;
    @NotBlank(message = "Room type is required.")
    private String roomType;
    @NotNull(message = "Block floor is required.")
    @Positive(message = "Block floor must be positive.")
    private Integer blockFloor;
    @NotNull(message = "Block code is required.")
    @Positive(message = "Block code must be positive.")
    private Integer blockCode;
    @NotNull(message = "Room availability flag is required.")
    private Boolean unavailable;


}
