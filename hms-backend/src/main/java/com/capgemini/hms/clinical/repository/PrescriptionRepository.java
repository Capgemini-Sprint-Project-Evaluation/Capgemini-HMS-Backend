package com.capgemini.hms.clinical.repository;

import com.capgemini.hms.clinical.entity.Prescription;
import com.capgemini.hms.clinical.entity.PrescriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionId> {
    List<Prescription> findByIdPatient(Integer patientSsn);
    List<Prescription> findByIdPhysician(Integer physicianId);
}
