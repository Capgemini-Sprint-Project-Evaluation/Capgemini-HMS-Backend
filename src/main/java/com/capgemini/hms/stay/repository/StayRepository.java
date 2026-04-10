package com.capgemini.hms.stay.repository;

import com.capgemini.hms.stay.entity.Stay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    @Query("SELECT s FROM Stay s WHERE s.isDeleted = false")
    Page<Stay> findAllActive(Pageable pageable);

    @Query("SELECT s FROM Stay s WHERE s.isDeleted = false AND s.stayEnd IS NULL")
    List<Stay> findByStayEndIsNullActive();

    @Query("SELECT s FROM Stay s WHERE s.isDeleted = false AND s.patient.ssn = :ssn")
    List<Stay> findByPatientSsnActive(@Param("ssn") Integer ssn);
}
