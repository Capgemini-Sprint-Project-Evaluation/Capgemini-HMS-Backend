package com.capgemini.hms.patient.repository;

import com.capgemini.hms.patient.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    Optional<Stay> findByStayIdAndIsDeletedFalse(Integer stayId);
    java.util.List<Stay> findByStayEndIsNullAndIsDeletedFalse();

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(MAX(s.stayId), 0) FROM Stay s")
    Integer findMaxStayId();
}
