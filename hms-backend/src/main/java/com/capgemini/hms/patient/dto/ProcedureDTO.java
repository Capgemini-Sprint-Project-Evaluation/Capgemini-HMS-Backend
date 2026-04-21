package com.capgemini.hms.patient.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public class ProcedureDTO {
    @NotNull(message = "Procedure code is required.")
    @Positive(message = "Procedure code must be positive.")
    private Integer code;
    @NotBlank(message = "Procedure name is required.")
    @Size(max = 255, message = "Procedure name must not exceed 255 characters.")
    private String name;
    @NotNull(message = "Procedure cost is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Procedure cost must be greater than 0.")
    private java.math.BigDecimal cost;

    public ProcedureDTO() {}
    public ProcedureDTO(Integer code, String name, java.math.BigDecimal cost) {
        this.code = code;
        this.name = name;
        this.cost = cost;
    }

    // Manual Accessors
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public java.math.BigDecimal getCost() { return cost; }
    public void setCost(java.math.BigDecimal cost) { this.cost = cost; }

    // Manual Builder
    public static class ProcedureDTOBuilder {
        private Integer code;
        private String name;
        private java.math.BigDecimal cost;
        public ProcedureDTOBuilder code(Integer code) { this.code = code; return this; }
        public ProcedureDTOBuilder name(String name) { this.name = name; return this; }
        public ProcedureDTOBuilder cost(java.math.BigDecimal cost) { this.cost = cost; return this; }
        public ProcedureDTO build() { return new ProcedureDTO(code, name, cost); }
    }
    public static ProcedureDTOBuilder builder() { return new ProcedureDTOBuilder(); }
}
