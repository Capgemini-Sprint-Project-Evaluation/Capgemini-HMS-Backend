package com.capgemini.hms.reporting.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DepartmentStatsDTO {
    @Schema(example = "General Medicine", description = "Name of the hospital department")
    private String departmentName;

    @Schema(example = "Dr. Alice Morgan", description = "Name of the Department Head")
    private String headName;

    @Schema(example = "12", description = "Number of physicians affiliated with this department")
    private long physicianCount;

    public DepartmentStatsDTO() {}

    public DepartmentStatsDTO(String departmentName, String headName, long physicianCount) {
        this.departmentName = departmentName;
        this.headName = headName;
        this.physicianCount = physicianCount;
    }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getHeadName() { return headName; }
    public void setHeadName(String headName) { this.headName = headName; }

    public long getPhysicianCount() { return physicianCount; }
    public void setPhysicianCount(long physicianCount) { this.physicianCount = physicianCount; }
}
