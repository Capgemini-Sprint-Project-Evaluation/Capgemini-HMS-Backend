package com.capgemini.hms.patient.repository;

import com.capgemini.hms.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findBySsnAndIsDeletedFalse(Integer ssn);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Patient p WHERE p.isDeleted = false AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR p.phone LIKE CONCAT('%', :query, '%'))")
    java.util.List<Patient> searchActivePatients(@org.springframework.data.repository.query.Param("query") String query);
}
