package com.capgemini.hms.prescription.repository;

import com.capgemini.hms.prescription.entity.Prescription;
import com.capgemini.hms.prescription.entity.PrescriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionId> {
    List<Prescription> findByPatient_Ssn(Integer ssn);
    List<Prescription> findByPhysician_EmployeeId(Integer employeeId);
}
