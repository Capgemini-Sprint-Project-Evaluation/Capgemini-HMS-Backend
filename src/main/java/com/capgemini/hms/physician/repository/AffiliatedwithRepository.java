package com.capgemini.hms.physician.repository;

import com.capgemini.hms.physician.entity.AffiliatedWith;
import com.capgemini.hms.physician.entity.AffiliatedWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {
    List<AffiliatedWith> findByPhysician_EmployeeId(Integer employeeId);
    List<AffiliatedWith> findByDepartment_DepartmentId(Integer departmentId);
    long countByDepartment_DepartmentId(Integer departmentId);
    List<AffiliatedWith> findByPrimaryAffiliationTrue();
}
