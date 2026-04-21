package com.capgemini.hms.clinical.repository;

import com.capgemini.hms.clinical.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    Optional<Medication> findByCodeAndIsDeletedFalse(Integer code);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Medication m WHERE m.isDeleted = false AND (LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.brand) LIKE LOWER(CONCAT('%', :query, '%')))")
    java.util.List<Medication> searchActiveMedications(@org.springframework.data.repository.query.Param("query") String query);
}
