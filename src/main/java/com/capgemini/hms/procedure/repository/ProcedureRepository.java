package com.capgemini.hms.procedure.repository;

import com.capgemini.hms.procedure.entity.Procedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {
    @Query("SELECT p FROM Procedure p WHERE p.isDeleted = false")
    Page<Procedure> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Procedure p WHERE p.isDeleted = false AND LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Procedure> searchActive(@Param("query") String query, Pageable pageable);
}
