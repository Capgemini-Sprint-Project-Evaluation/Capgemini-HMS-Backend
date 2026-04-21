package com.capgemini.hms.auth.repository;

import com.capgemini.hms.auth.entity.AffiliatedWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, Object> {
    List<AffiliatedWith> findByDepartmentDepartmentIdAndIsDeletedFalse(Integer departmentId);
    long countByDepartmentDepartmentIdAndIsDeletedFalse(Integer departmentId);
}
