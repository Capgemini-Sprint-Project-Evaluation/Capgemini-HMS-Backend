package com.capgemini.hms.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PatientDTO {
    @NotNull(message = "SSN is required")
    @Positive(message = "SSN must be positive.")
    private Integer ssn;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$", message = "Phone number format is invalid.")
    private String phone;

    @NotBlank(message = "Insurance ID is required")
    private String insuranceId;

    @NotNull(message = "PCP ID is required")
    @Positive(message = "PCP ID must be positive.")
    private Integer pcpId;

    public PatientDTO() {}
    public PatientDTO(Integer ssn, String name, String address, String phone, String insuranceId, Integer pcpId) {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.pcpId = pcpId;
    }

    // Manual Accessors
    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getInsuranceId() { return insuranceId; }
    public void setInsuranceId(String insuranceId) { this.insuranceId = insuranceId; }
    public Integer getPcpId() { return pcpId; }
    public void setPcpId(Integer pcpId) { this.pcpId = pcpId; }

    // Manual Builder
    public static class PatientDTOBuilder {
        private Integer ssn;
        private String name;
        private String address;
        private String phone;
        private String insuranceId;
        private Integer pcpId;
        public PatientDTOBuilder ssn(Integer ssn) { this.ssn = ssn; return this; }
        public PatientDTOBuilder name(String name) { this.name = name; return this; }
        public PatientDTOBuilder address(String address) { this.address = address; return this; }
        public PatientDTOBuilder phone(String phone) { this.phone = phone; return this; }
        public PatientDTOBuilder insuranceId(String insuranceId) { this.insuranceId = insuranceId; return this; }
        public PatientDTOBuilder pcpId(Integer pcpId) { this.pcpId = pcpId; return this; }
        public PatientDTO build() { return new PatientDTO(ssn, name, address, phone, insuranceId, pcpId); }
    }
    public static PatientDTOBuilder builder() { return new PatientDTOBuilder(); }
}
