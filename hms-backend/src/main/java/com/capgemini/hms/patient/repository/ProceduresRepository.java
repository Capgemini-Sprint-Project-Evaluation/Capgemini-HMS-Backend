package com.capgemini.hms.patient.repository;

import com.capgemini.hms.patient.entity.Procedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedures, Integer> {
    Optional<Procedures> findByCodeAndIsDeletedFalse(Integer code);
}
