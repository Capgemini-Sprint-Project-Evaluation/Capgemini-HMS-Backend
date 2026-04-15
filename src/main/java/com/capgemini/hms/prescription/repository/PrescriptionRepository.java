package com.capgemini.hms.prescription.repository;

import com.capgemini.hms.prescription.entity.Prescription;
import com.capgemini.hms.prescription.entity.PrescriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PrescriptionRepository provides standard JpaRepository operations for the Prescription entity.
 * It includes custom finders to retrieve prescriptions by patient SSN or physician ID.
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionId> {
    /**
     * Finds all prescriptions associated with a specific patient.
     * @param ssn The social security number of the patient.
     * @return A list of prescriptions.
     */
    List<Prescription> findByPatient_Ssn(Integer ssn);

    /**
     * Finds all prescriptions issued by a specific physician.
     * @param employeeId The employee ID of the physician.
     * @return A list of prescriptions.
     */
    List<Prescription> findByPhysician_EmployeeId(Integer employeeId);
}
