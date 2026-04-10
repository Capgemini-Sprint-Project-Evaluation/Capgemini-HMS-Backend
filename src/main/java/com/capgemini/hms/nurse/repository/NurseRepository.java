package com.capgemini.hms.nurse.repository;

import com.capgemini.hms.nurse.entity.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    @Query("SELECT n FROM Nurse n WHERE n.isDeleted = false")
    Page<Nurse> findAllActive(Pageable pageable);

    @Query("SELECT n FROM Nurse n WHERE n.isDeleted = false AND (LOWER(n.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.position) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Nurse> searchActive(@Param("query") String query, Pageable pageable);

    @Query("SELECT n FROM Nurse n WHERE n.isDeleted = false AND n.registered = :registered")
    List<Nurse> findByRegisteredActive(@Param("registered") Boolean registered);
}
