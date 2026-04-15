package com.capgemini.hms.appointment.repository;

import com.capgemini.hms.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a WHERE a.isDeleted = false")
    Page<Appointment> findAllActive(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.isDeleted = false AND a.physician.employeeId = :physicianId")
    List<Appointment> findByPhysicianActive(@Param("physicianId") Integer physicianId);

    @Query("SELECT a FROM Appointment a WHERE a.isDeleted = false AND a.patient.ssn = :patientSsn")
    List<Appointment> findByPatientActive(@Param("patientSsn") Integer patientSsn);

    @Query("SELECT a FROM Appointment a WHERE a.isDeleted = false AND a.physician.employeeId = :physicianId AND ((a.start < :end AND a.end > :start))")
    List<Appointment> findOverlappingPhysicianAppointments(@Param("physicianId") Integer physicianId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.isDeleted = false AND a.examinationRoom = :room AND ((a.start < :end AND a.end > :start))")
    List<Appointment> findOverlappingRoomAppointments(@Param("room") String room, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    long countByStartBetween(LocalDateTime start, LocalDateTime end);
}
