package com.capgemini.hms.physician.repository;

import com.capgemini.hms.physician.entity.Physician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
    @Query("SELECT p FROM Physician p WHERE p.isDeleted = false")
    Page<Physician> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Physician p WHERE p.isDeleted = false AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.position) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Physician> searchActive(@Param("query") String query, Pageable pageable);

    Optional<Physician> findBySsn(Integer ssn);
}
