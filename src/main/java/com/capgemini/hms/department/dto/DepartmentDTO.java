package com.capgemini.hms.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DepartmentDTO {
    @NotNull
    @Schema(example = "1", description = "Unique ID of the department")
    private Integer departmentId;

    @NotBlank
    @Schema(example = "General Medicine", description = "Name of the hospital department")
    private String name;

    @Schema(example = "101", description = "Employee ID of the Department Head")
    private Integer headId;

    public DepartmentDTO() {}

    public DepartmentDTO(Integer departmentId, String name, Integer headId) {
        this.departmentId = departmentId;
        this.name = name;
        this.headId = headId;
    }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getHeadId() { return headId; }
    public void setHeadId(Integer headId) { this.headId = headId; }
}
