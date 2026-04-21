package com.capgemini.hms.nursing.repository;

import com.capgemini.hms.nursing.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    Optional<Nurse> findByEmployeeIdAndIsDeletedFalse(Integer employeeId);

    @org.springframework.data.jpa.repository.Query("SELECT n FROM Nurse n WHERE n.isDeleted = false AND (LOWER(n.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.position) LIKE LOWER(CONCAT('%', :query, '%')))")
    java.util.List<Nurse> searchActiveNurses(@org.springframework.data.repository.query.Param("query") String query);
}
