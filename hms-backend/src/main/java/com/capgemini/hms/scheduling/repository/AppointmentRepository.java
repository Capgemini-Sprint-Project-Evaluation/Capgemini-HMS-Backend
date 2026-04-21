package com.capgemini.hms.scheduling.repository;

import com.capgemini.hms.scheduling.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Optional<Appointment> findByAppointmentIdAndIsDeletedFalse(Integer appointmentId);
    List<Appointment> findByPatientSsnAndIsDeletedFalse(Integer patientSsn);
    List<Appointment> findByPhysicianEmployeeIdAndIsDeletedFalse(Integer physicianId);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM Appointment a WHERE a.isDeleted = false AND LOWER(a.examinationRoom) LIKE LOWER(CONCAT('%', :room, '%'))")
    List<Appointment> searchByRoom(@org.springframework.data.repository.query.Param("room") String room);
}
