package com.capgemini.hms.patient.repository;

import com.capgemini.hms.patient.entity.Undergoes;
import com.capgemini.hms.patient.entity.UndergoesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {
    List<Undergoes> findByPatientSsnAndIsDeletedFalse(Integer ssn);
}
