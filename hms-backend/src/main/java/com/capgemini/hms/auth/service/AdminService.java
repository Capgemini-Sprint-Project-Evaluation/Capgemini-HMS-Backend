package com.capgemini.hms.auth.service;

import com.capgemini.hms.auth.dto.DashboardSummaryDTO;
import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.repository.*;
import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.clinical.repository.MedicationRepository;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.patient.repository.StayRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.AppointmentRepository;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    private final NurseRepository nurseRepository;
    private final AppointmentRepository appointmentRepository;
    private final StayRepository stayRepository;
    private final MedicationRepository medicationRepository;
    private final DepartmentRepository departmentRepository;
    private final AffiliatedWithRepository affiliatedWithRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public DashboardSummaryDTO getDashboardSummary() {
        return DashboardSummaryDTO.builder()
                .totalPatients(patientRepository.count())
                .totalDoctors(physicianRepository.count())
                .totalNurses(nurseRepository.count())
                .totalPendingAppointments(appointmentRepository.count())
                .totalActiveStays(stayRepository.count())
                .totalMedications(medicationRepository.count())
                .build();
    }

    public List<com.capgemini.hms.auth.dto.DepartmentStatsDTO> getDepartmentStats() {
        return departmentRepository.findAll().stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .map(d -> com.capgemini.hms.auth.dto.DepartmentStatsDTO.builder()
                        .departmentId(d.getDepartmentId())
                        .departmentName(d.getName())
                        .physicianCount((int) affiliatedWithRepository.countByDepartmentDepartmentIdAndIsDeletedFalse(d.getDepartmentId()))
                        .build())
                .toList();
    }

    public List<com.capgemini.hms.auth.dto.AffiliationDTO> getAffiliationsByDepartment(Integer deptId) {
        return affiliatedWithRepository.findByDepartmentDepartmentIdAndIsDeletedFalse(deptId).stream()
                .map(aw -> com.capgemini.hms.auth.dto.AffiliationDTO.builder()
                        .physicianId(aw.getPhysician().getEmployeeId())
                        .physicianName(aw.getPhysician().getName())
                        .departmentId(aw.getDepartment().getDepartmentId())
                        .departmentName(aw.getDepartment().getName())
                        .primaryAffiliation(aw.getPrimaryaffiliation())
                        .build())
                .toList();
    }

    @Transactional
    public com.capgemini.hms.auth.dto.AffiliationDTO addAffiliation(com.capgemini.hms.auth.dto.AffiliationDTO dto) {
        Integer physicianId = dto.getPhysicianId();
        Integer departmentId = dto.getDepartmentId();

        if (physicianId == null || departmentId == null) {
             throw new com.capgemini.hms.exception.BadRequestException("Physician ID and Department ID are required.");
        }

        Physician physician = physicianRepository.findByEmployeeIdAndIsDeletedFalse(physicianId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PHYSICIAN_NOT_FOUND.formatted(physicianId)));
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND.formatted(departmentId)));

        com.capgemini.hms.auth.entity.AffiliatedWith aw = com.capgemini.hms.auth.entity.AffiliatedWith.builder()
                .id(new com.capgemini.hms.auth.entity.AffiliatedWithId(physician.getEmployeeId(), department.getDepartmentId()))
                .physician(physician)
                .department(department)
                .primaryaffiliation(dto.getPrimaryAffiliation())
                .build();
        
        affiliatedWithRepository.save(aw);
        return dto;
    }

    public List<com.capgemini.hms.auth.dto.AuditLogDTO> getAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(al -> com.capgemini.hms.auth.dto.AuditLogDTO.builder()
                        .id(al.getId())
                        .username(al.getUsername())
                        .action(al.getAction())
                        .timestamp(al.getTimestamp())
                        .details(al.getDetails())
                        .build())
                .toList();
    }

    public List<com.capgemini.hms.auth.entity.User> getAllUsers() {
        return userRepository.findAll();
    }
}
