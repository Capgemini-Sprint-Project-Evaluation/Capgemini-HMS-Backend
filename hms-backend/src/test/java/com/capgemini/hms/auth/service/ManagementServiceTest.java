package com.capgemini.hms.auth.service;

import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.repository.DepartmentRepository;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ConflictException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private ManagementService managementService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(101);
        department.setName("Cardiology");
        department.setIsDeleted(false);
    }

    @Test
    void getAllDepartments_ShouldReturnList() {
        when(departmentRepository.findByIsDeletedFalse()).thenReturn(Arrays.asList(department));

        List<Department> result = managementService.getAllDepartments();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getName());
    }

    @Test
    void getDepartmentById_Success() {
        when(departmentRepository.findById(101)).thenReturn(Optional.of(department));

        Department result = managementService.getDepartmentById(101);

        assertNotNull(result);
        assertEquals(101, result.getDepartmentId());
    }

    @Test
    void getDepartmentById_NotFound() {
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            managementService.getDepartmentById(999);
        });
    }

    @Test
    void saveDepartment_Success() {
        when(departmentRepository.existsById(101)).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = managementService.saveDepartment(department);

        assertNotNull(result);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void saveDepartment_Conflict() {
        when(departmentRepository.existsById(101)).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            managementService.saveDepartment(department);
        });
    }

    @Test
    void saveDepartment_NoId() {
        department.setDepartmentId(null);
        assertThrows(BadRequestException.class, () -> {
            managementService.saveDepartment(department);
        });
    }

    @Test
    void deleteDepartment_Success() {
        when(departmentRepository.findById(101)).thenReturn(Optional.of(department));

        managementService.deleteDepartment(101);

        assertTrue(department.getIsDeleted());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void updateDepartment_Success() {
        Department updated = new Department();
        updated.setName("Updated");
        updated.setHeadId(202);
        when(departmentRepository.findById(101)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = managementService.updateDepartment(101, updated);

        assertEquals("Updated", result.getName());
        assertEquals(202, result.getHeadId());
    }
}
