package com.capgemini.hms.patient.repository;

import com.capgemini.hms.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {}
