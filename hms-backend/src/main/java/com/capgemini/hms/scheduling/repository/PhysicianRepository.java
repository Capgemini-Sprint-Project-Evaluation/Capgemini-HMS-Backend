package com.capgemini.hms.scheduling.repository;

import com.capgemini.hms.scheduling.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
    Optional<Physician> findByEmployeeIdAndIsDeletedFalse(Integer employeeId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Physician p WHERE p.isDeleted = false AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.position) LIKE LOWER(CONCAT('%', :query, '%')))")
    java.util.List<Physician> searchActivePhysicians(@org.springframework.data.repository.query.Param("query") String query);
}
