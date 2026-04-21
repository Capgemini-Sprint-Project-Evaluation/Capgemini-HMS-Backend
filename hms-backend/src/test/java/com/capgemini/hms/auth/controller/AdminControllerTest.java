package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.dto.AffiliationDTO;
import com.capgemini.hms.auth.dto.AuditLogDTO;
import com.capgemini.hms.auth.dto.DashboardSummaryDTO;
import com.capgemini.hms.auth.dto.DepartmentStatsDTO;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.service.AdminService;
import com.capgemini.hms.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getDashboardSummary_shouldReturnOkResponse() {
        DashboardSummaryDTO dto = DashboardSummaryDTO.builder().totalPatients(10L).build();
        when(adminService.getDashboardSummary()).thenReturn(dto);

        ResponseEntity<ApiResponse<DashboardSummaryDTO>> response = adminController.getDashboardSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void getDepartmentStats_shouldReturnStats() {
        List<DepartmentStatsDTO> stats = List.of(DepartmentStatsDTO.builder().departmentName("Cardiology").build());
        when(adminService.getDepartmentStats()).thenReturn(stats);

        ResponseEntity<ApiResponse<List<DepartmentStatsDTO>>> response = adminController.getDepartmentStats();

        assertEquals(stats, response.getBody().getData());
    }

    @Test
    void getAffiliations_shouldDelegateToService() {
        List<AffiliationDTO> affiliations = List.of(AffiliationDTO.builder().departmentId(1).build());
        when(adminService.getAffiliationsByDepartment(1)).thenReturn(affiliations);

        ResponseEntity<ApiResponse<List<AffiliationDTO>>> response = adminController.getAffiliations(1);

        assertEquals(affiliations, response.getBody().getData());
    }

    @Test
    void addAffiliation_shouldReturnCreatedResponse() {
        AffiliationDTO dto = AffiliationDTO.builder().departmentId(1).physicianId(2).build();
        when(adminService.addAffiliation(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<AffiliationDTO>> response = adminController.addAffiliation(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void getAllUsers_shouldReturnUsers() {
        List<User> users = List.of(User.builder().username("admin").build());
        when(adminService.getAllUsers()).thenReturn(users);

        ResponseEntity<ApiResponse<List<User>>> response = adminController.getAllUsers();

        assertEquals(users, response.getBody().getData());
    }

    @Test
    void getAuditLogs_shouldReturnLogs() {
        List<AuditLogDTO> logs = List.of(AuditLogDTO.builder().username("admin").build());
        when(adminService.getAuditLogs()).thenReturn(logs);

        ResponseEntity<ApiResponse<List<AuditLogDTO>>> response = adminController.getAuditLogs();

        assertEquals(logs, response.getBody().getData());
        verify(adminService).getAuditLogs();
    }
}
