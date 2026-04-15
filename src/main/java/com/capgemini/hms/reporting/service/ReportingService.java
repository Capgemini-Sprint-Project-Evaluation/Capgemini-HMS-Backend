package com.capgemini.hms.reporting.service;

import com.capgemini.hms.appointment.repository.AppointmentRepository;
import com.capgemini.hms.department.entity.Department;
import com.capgemini.hms.department.repository.DepartmentRepository;
import com.capgemini.hms.nurse.repository.NurseRepository;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.physician.repository.AffiliatedWithRepository;
import com.capgemini.hms.physician.repository.PhysicianRepository;
import com.capgemini.hms.procedure.repository.UndergoesRepository;
import com.capgemini.hms.reporting.dto.DashboardSummaryDTO;
import com.capgemini.hms.reporting.dto.DepartmentStatsDTO;
import com.capgemini.hms.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    private final NurseRepository nurseRepository;
    private final RoomRepository roomRepository;
    private final AppointmentRepository appointmentRepository;
    private final UndergoesRepository undergoesRepository;
    private final DepartmentRepository departmentRepository;
    private final AffiliatedWithRepository affiliatedWithRepository;

    public ReportingService(PatientRepository patientRepository,
                            PhysicianRepository physicianRepository,
                            NurseRepository nurseRepository,
                            RoomRepository roomRepository,
                            AppointmentRepository appointmentRepository,
                            UndergoesRepository undergoesRepository,
                            DepartmentRepository departmentRepository,
                            AffiliatedWithRepository affiliatedWithRepository) {
        this.patientRepository = patientRepository;
        this.physicianRepository = physicianRepository;
        this.nurseRepository = nurseRepository;
        this.roomRepository = roomRepository;
        this.appointmentRepository = appointmentRepository;
        this.undergoesRepository = undergoesRepository;
        this.departmentRepository = departmentRepository;
        this.affiliatedWithRepository = affiliatedWithRepository;
    }

    public DashboardSummaryDTO getSummary() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Double revenue = undergoesRepository.calculateTotalRevenue();
        if (revenue == null) revenue = 0.0;

        return new DashboardSummaryDTO(
                patientRepository.count(),
                physicianRepository.count(),
                nurseRepository.count(),
                roomRepository.countByUnavailableTrue(),
                roomRepository.count(),
                revenue,
                appointmentRepository.countByStartBetween(startOfDay, endOfDay)
        );
    }

    public List<DepartmentStatsDTO> getDepartmentStats() {
        return departmentRepository.findAll().stream().map(dept -> {
            long physicianCount = affiliatedWithRepository.countByDepartment_DepartmentId(dept.getDepartmentId());
            String headName = dept.getHead() != null ? dept.getHead().getName() : "No Head Assigned";
            return new DepartmentStatsDTO(dept.getName(), headName, physicianCount);
        }).collect(Collectors.toList());
    }
}
