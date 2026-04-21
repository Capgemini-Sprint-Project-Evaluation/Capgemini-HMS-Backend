package com.capgemini.hms.auth.controller;

import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.service.ManagementService;
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
class ManagementControllerTest {

    @Mock
    private ManagementService managementService;

    @InjectMocks
    private ManagementController managementController;

    @Test
    void getAllDepartments_shouldReturnDepartments() {
        List<Department> departments = List.of(Department.builder().departmentId(1).name("Cardiology").build());
        when(managementService.getAllDepartments()).thenReturn(departments);

        ResponseEntity<ApiResponse<List<Department>>> response = managementController.getAllDepartments();

        assertEquals(departments, response.getBody().getData());
    }

    @Test
    void getDepartmentById_shouldReturnDepartment() {
        Department department = Department.builder().departmentId(1).name("Cardiology").build();
        when(managementService.getDepartmentById(1)).thenReturn(department);

        ResponseEntity<ApiResponse<Department>> response = managementController.getDepartmentById(1);

        assertEquals(department, response.getBody().getData());
    }

    @Test
    void createDepartment_shouldReturnCreatedResponse() {
        Department department = Department.builder().departmentId(1).name("Cardiology").build();
        when(managementService.saveDepartment(department)).thenReturn(department);

        ResponseEntity<ApiResponse<Department>> response = managementController.createDepartment(department);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(department, response.getBody().getData());
    }

    @Test
    void updateDepartment_shouldReturnUpdatedDepartment() {
        Department department = Department.builder().departmentId(1).name("Updated").build();
        when(managementService.updateDepartment(1, department)).thenReturn(department);

        ResponseEntity<ApiResponse<Department>> response = managementController.updateDepartment(1, department);

        assertEquals(department, response.getBody().getData());
    }

    @Test
    void deleteDepartment_shouldDelegateAndReturnSuccess() {
        ResponseEntity<ApiResponse<String>> response = managementController.deleteDepartment(1);

        verify(managementService).deleteDepartment(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
