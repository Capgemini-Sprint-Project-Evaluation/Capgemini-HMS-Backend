package com.capgemini.hms.procedure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProcedureDTO {
    @NotNull
    @Schema(example = "1", description = "Unique code for the medical procedure")
    private Integer code;

    @NotBlank
    @Schema(example = "Appendectomy", description = "Official name of the procedure")
    private String name;

    @NotNull
    @Positive
    @Schema(example = "1500.50", description = "Cost of the procedure in local currency")
    private Double cost;

    public ProcedureDTO() {}

    public ProcedureDTO(Integer code, String name, Double cost) {
        this.code = code;
        this.name = name;
        this.cost = cost;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}
