package com.capgemini.hms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {
    private long totalPatients;
    private long totalDoctors;
    private long totalNurses;
    private long totalPendingAppointments;
    private long totalActiveStays;
    private long totalDepartments;
    private long totalMedications;


}
