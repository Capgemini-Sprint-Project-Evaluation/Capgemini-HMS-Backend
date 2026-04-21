package com.capgemini.hms.auth.service;

import com.capgemini.hms.auth.dto.AffiliationDTO;
import com.capgemini.hms.auth.dto.DashboardSummaryDTO;
import com.capgemini.hms.auth.entity.AuditLog;
import com.capgemini.hms.auth.entity.Department;
import com.capgemini.hms.auth.entity.User;
import com.capgemini.hms.auth.repository.AffiliatedWithRepository;
import com.capgemini.hms.auth.repository.AuditLogRepository;
import com.capgemini.hms.auth.repository.DepartmentRepository;
import com.capgemini.hms.auth.repository.UserRepository;
import com.capgemini.hms.clinical.repository.MedicationRepository;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.patient.repository.StayRepository;
import com.capgemini.hms.scheduling.repository.AppointmentRepository;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PhysicianRepository physicianRepository;
    @Mock
    private NurseRepository nurseRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private StayRepository stayRepository;
    @Mock
    private MedicationRepository medicationRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private AffiliatedWithRepository affiliatedWithRepository;
    @Mock
    private AuditLogRepository auditLogRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getDashboardSummary_shouldAggregateRepositoryCounts() {
        when(patientRepository.count()).thenReturn(10L);
        when(physicianRepository.count()).thenReturn(5L);
        when(nurseRepository.count()).thenReturn(7L);
        when(appointmentRepository.count()).thenReturn(4L);
        when(stayRepository.count()).thenReturn(3L);
        when(medicationRepository.count()).thenReturn(9L);

        DashboardSummaryDTO result = adminService.getDashboardSummary();

        assertEquals(10L, result.getTotalPatients());
        assertEquals(5L, result.getTotalDoctors());
        assertEquals(7L, result.getTotalNurses());
        assertEquals(4L, result.getTotalPendingAppointments());
        assertEquals(3L, result.getTotalActiveStays());
        assertEquals(9L, result.getTotalMedications());
    }

    @Test
    void addAffiliation_shouldThrowWhenIdsAreMissing() {
        AffiliationDTO dto = AffiliationDTO.builder().build();

        assertThrows(BadRequestException.class, () -> adminService.addAffiliation(dto));
    }

    @Test
    void addAffiliation_shouldPersistWhenPhysicianAndDepartmentExist() {
        AffiliationDTO dto = AffiliationDTO.builder()
                .physicianId(11)
                .departmentId(22)
                .primaryAffiliation(true)
                .build();

        com.capgemini.hms.scheduling.entity.Physician physician =
                com.capgemini.hms.scheduling.entity.Physician.builder().employeeId(11).name("Dr. A").build();
        Department department = Department.builder().departmentId(22).name("Cardiology").build();

        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(11)).thenReturn(Optional.of(physician));
        when(departmentRepository.findById(22)).thenReturn(Optional.of(department));

        AffiliationDTO result = adminService.addAffiliation(dto);

        assertNotNull(result);
        verify(affiliatedWithRepository).save(any(com.capgemini.hms.auth.entity.AffiliatedWith.class));
    }

    @Test
    void addAffiliation_shouldThrowWhenDepartmentDoesNotExist() {
        AffiliationDTO dto = AffiliationDTO.builder()
                .physicianId(11)
                .departmentId(22)
                .build();

        com.capgemini.hms.scheduling.entity.Physician physician =
                com.capgemini.hms.scheduling.entity.Physician.builder().employeeId(11).build();

        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(11)).thenReturn(Optional.of(physician));
        when(departmentRepository.findById(22)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addAffiliation(dto));
    }

    @Test
    void getAuditLogs_shouldMapEntitiesToDtos() {
        AuditLog log = AuditLog.builder()
                .id(1L)
                .username("admin")
                .action("CREATE")
                .details("created")
                .timestamp(java.time.LocalDateTime.now())
                .build();
        when(auditLogRepository.findAll()).thenReturn(List.of(log));

        List<com.capgemini.hms.auth.dto.AuditLogDTO> result = adminService.getAuditLogs();

        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUsername());
    }

    @Test
    void getAllUsers_shouldReturnRepositoryResult() {
        when(userRepository.findAll()).thenReturn(List.of(User.builder().username("admin").build()));

        List<User> result = adminService.getAllUsers();

        assertEquals(1, result.size());
    }

    @Test
    void getDepartmentStats_shouldMapActiveDepartments() {
        Department department = Department.builder().departmentId(22).name("Cardiology").isDeleted(false).build();
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(affiliatedWithRepository.countByDepartmentDepartmentIdAndIsDeletedFalse(22)).thenReturn(3L);

        List<com.capgemini.hms.auth.dto.DepartmentStatsDTO> result = adminService.getDepartmentStats();

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getPhysicianCount());
    }

    @Test
    void getAffiliationsByDepartment_shouldMapAffiliations() {
        com.capgemini.hms.scheduling.entity.Physician physician =
                com.capgemini.hms.scheduling.entity.Physician.builder().employeeId(11).name("Dr. A").build();
        Department department = Department.builder().departmentId(22).name("Cardiology").build();
        com.capgemini.hms.auth.entity.AffiliatedWith affiliatedWith = com.capgemini.hms.auth.entity.AffiliatedWith.builder()
                .physician(physician)
                .department(department)
                .primaryaffiliation(true)
                .build();
        when(affiliatedWithRepository.findByDepartmentDepartmentIdAndIsDeletedFalse(22))
                .thenReturn(List.of(affiliatedWith));

        List<com.capgemini.hms.auth.dto.AffiliationDTO> result = adminService.getAffiliationsByDepartment(22);

        assertEquals(1, result.size());
        assertEquals(11, result.get(0).getPhysicianId());
    }
}
