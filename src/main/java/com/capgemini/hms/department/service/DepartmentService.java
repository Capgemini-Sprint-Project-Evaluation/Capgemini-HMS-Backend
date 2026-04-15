package com.capgemini.hms.department.service;

import com.capgemini.hms.department.entity.Department;
import com.capgemini.hms.department.repository.DepartmentRepository;
import com.capgemini.hms.physician.entity.AffiliatedWith;
import com.capgemini.hms.physician.entity.AffiliatedWithId;
import com.capgemini.hms.physician.entity.Physician;
import com.capgemini.hms.physician.repository.AffiliatedWithRepository;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final PhysicianRepository physicianRepository;
    private final AffiliatedWithRepository affiliatedWithRepository;

    public DepartmentService(DepartmentRepository departmentRepository, 
                             PhysicianRepository physicianRepository,
                             AffiliatedWithRepository affiliatedWithRepository) {
        this.departmentRepository = departmentRepository;
        this.physicianRepository = physicianRepository;
        this.affiliatedWithRepository = affiliatedWithRepository;
    }

    // --- Department CRUD ---

    @Transactional
    public Department saveDepartment(Department department) {
        department.setIsDeleted(false);
        return departmentRepository.save(department);
    }

    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAllActive(pageable);
    }

    public Optional<Department> getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .filter(d -> !d.getIsDeleted());
    }

    @Transactional
    public Department updateDepartment(Department department) {
        Department existing = departmentRepository.findById(department.getDepartmentId())
                .filter(d -> !d.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        existing.setName(department.getName());
        existing.setHead(department.getHead());
        
        return departmentRepository.save(existing);
    }

    @Transactional
    public void deleteDepartment(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        dept.setIsDeleted(true);
        departmentRepository.save(dept);
    }

    // --- Head Management ---

    @Transactional
    public Department setDepartmentHead(Integer departmentId, Integer physicianId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Physician head = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        
        dept.setHead(head);
        return departmentRepository.save(dept);
    }

    // --- Affiliation Management ---

    @Transactional
    public AffiliatedWith affiliatePhysician(Integer physicianId, Integer departmentId, Boolean isPrimary) {
        Physician physician = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new RuntimeException("Physician not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        AffiliatedWithId id = new AffiliatedWithId(physicianId, departmentId);
        AffiliatedWith affiliation = new AffiliatedWith(id, physician, department, isPrimary);
        
        return affiliatedWithRepository.save(affiliation);
    }

    public List<AffiliatedWith> getPhysicianAffiliations(Integer physicianId) {
        return affiliatedWithRepository.findByPhysician_EmployeeId(physicianId);
    }

    public List<AffiliatedWith> getDepartmentPhysicians(Integer departmentId) {
        return affiliatedWithRepository.findByDepartment_DepartmentId(departmentId);
    }
}
