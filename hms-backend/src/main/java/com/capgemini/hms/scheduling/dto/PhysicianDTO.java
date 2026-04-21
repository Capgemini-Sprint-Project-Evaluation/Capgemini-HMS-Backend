package com.capgemini.hms.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PhysicianDTO {
    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be positive.")
    private Integer employeeId;

    @NotBlank(message = "Physician name is required")
    @Size(max = 255, message = "Physician name must not exceed 255 characters.")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "SSN is required")
    @Positive(message = "SSN must be positive.")
    private Integer ssn;

    public PhysicianDTO() {}
    public PhysicianDTO(Integer employeeId, String name, String position, Integer ssn) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.ssn = ssn;
    }

    // Manual Accessors
    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }

    // Manual Builder
    public static class PhysicianDTOBuilder {
        private Integer employeeId;
        private String name;
        private String position;
        private Integer ssn;
        public PhysicianDTOBuilder employeeId(Integer employeeId) { this.employeeId = employeeId; return this; }
        public PhysicianDTOBuilder name(String name) { this.name = name; return this; }
        public PhysicianDTOBuilder position(String position) { this.position = position; return this; }
        public PhysicianDTOBuilder ssn(Integer ssn) { this.ssn = ssn; return this; }
        public PhysicianDTO build() { return new PhysicianDTO(employeeId, name, position, ssn); }
    }
    public static PhysicianDTOBuilder builder() { return new PhysicianDTOBuilder(); }
}
