package com.capgemini.hms.auth.service;

import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.repository.DepartmentRepository;
import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ConflictException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final DepartmentRepository departmentRepository;


    public List<Department> getAllDepartments() {
        return departmentRepository.findByIsDeletedFalse();
    }

    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .filter(department -> !Boolean.TRUE.equals(department.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND.formatted(id)));
    }

    public Department saveDepartment(Department department) {
        if (department.getDepartmentId() == null) {
            throw new BadRequestException("Department ID is required.");
        }
        if (departmentRepository.existsById(department.getDepartmentId())) {
            throw new ConflictException(ErrorMessages.DEPARTMENT_ALREADY_EXISTS.formatted(department.getDepartmentId()));
        }
        department.setIsDeleted(false);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Integer id, Department department) {
        return departmentRepository.findById(id).map(existing -> {
            existing.setName(department.getName());
            existing.setHeadId(department.getHeadId());
            return departmentRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND.formatted(id)));
    }

    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id)
                .filter(dept -> !Boolean.TRUE.equals(dept.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND.formatted(id)));
        department.setIsDeleted(true);
        departmentRepository.save(department);
    }
}
