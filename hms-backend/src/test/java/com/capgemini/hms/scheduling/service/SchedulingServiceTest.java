package com.capgemini.hms.scheduling.service;

import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.nursing.entity.Nurse;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.scheduling.dto.AppointmentDTO;
import com.capgemini.hms.scheduling.dto.PhysicianDTO;
import com.capgemini.hms.scheduling.entity.Appointment;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.AppointmentRepository;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import com.capgemini.hms.scheduling.repository.TrainedInRepository;
import com.capgemini.hms.patient.repository.ProceduresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock private PhysicianRepository physicianRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private NurseRepository nurseRepository;
    @Mock private TrainedInRepository trainedInRepository;
    @Mock private ProceduresRepository proceduresRepository;

    @InjectMocks
    private SchedulingService schedulingService;

    private Physician physician;
    private PhysicianDTO physicianDTO;

    @BeforeEach
    void setUp() {
        physician = Physician.builder()
                .employeeId(101)
                .name("Dr. Strange")
                .position("Surgeon")
                .ssn(999888)
                .isDeleted(false)
                .build();

        physicianDTO = PhysicianDTO.builder()
                .employeeId(101)
                .name("Dr. Strange")
                .position("Surgeon")
                .ssn(999888)
                .build();
    }

    @Test
    void getAllPhysicians_ShouldReturnList() {
        when(physicianRepository.findAll()).thenReturn(Arrays.asList(physician));
        List<PhysicianDTO> result = schedulingService.getAllPhysicians();
        assertFalse(result.isEmpty());
        assertEquals("Dr. Strange", result.get(0).getName());
    }

    @Test
    void getPhysicianById_Success() {
        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        PhysicianDTO result = schedulingService.getPhysicianById(101);
        assertNotNull(result);
        assertEquals(101, result.getEmployeeId());
    }

    @Test
    void bookAppointment_Success() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        AppointmentDTO dto = AppointmentDTO.builder()
                .appointmentId(5001)
                .patientSsn(1001)
                .physicianId(101)
                .prepNurseId(201)
                .start(start)
                .end(end)
                .examinationRoom("Room A")
                .build();

        Patient patient = Patient.builder().ssn(1001).isDeleted(false).build();
        Nurse nurse = Nurse.builder().employeeId(201).isDeleted(false).build();

        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        when(nurseRepository.findByEmployeeIdAndIsDeletedFalse(201)).thenReturn(Optional.of(nurse));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        AppointmentDTO result = schedulingService.bookAppointment(dto);

        assertNotNull(result);
        assertEquals(5001, result.getAppointmentId());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_InvalidTimes() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(1)) // End before start
                .build();

        assertThrows(BadRequestException.class, () -> schedulingService.bookAppointment(dto));
    }

    @Test
    void cancelAppointment_Success() {
        Appointment appointment = Appointment.builder().appointmentId(5001).isDeleted(false).build();
        when(appointmentRepository.findByAppointmentIdAndIsDeletedFalse(5001)).thenReturn(Optional.of(appointment));
        
        schedulingService.cancelAppointment(5001);
        
        assertTrue(appointment.getIsDeleted());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void searchPhysicians_ShouldReturnMappedResults() {
        when(physicianRepository.searchActivePhysicians("strange")).thenReturn(List.of(physician));

        List<PhysicianDTO> result = schedulingService.searchPhysicians("strange");

        assertEquals(1, result.size());
        assertEquals("Dr. Strange", result.get(0).getName());
    }

    @Test
    void getCertificationsByPhysician_ShouldMapResults() {
        com.capgemini.hms.patient.entity.Procedures procedure =
                com.capgemini.hms.patient.entity.Procedures.builder().code(1).name("Procedure").build();
        com.capgemini.hms.scheduling.entity.TrainedIn trainedIn =
                com.capgemini.hms.scheduling.entity.TrainedIn.builder()
                        .physician(physician)
                        .procedure(procedure)
                        .treatmentdate(LocalDateTime.now())
                        .certificationexpires(LocalDateTime.now().plusDays(1))
                        .build();
        when(trainedInRepository.findByPhysicianEmployeeIdAndIsDeletedFalse(101)).thenReturn(List.of(trainedIn));

        List<com.capgemini.hms.scheduling.dto.TrainedInDTO> result = schedulingService.getCertificationsByPhysician(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getPhysicianId());
    }

    @Test
    void addCertification_ShouldPersistCertification() {
        com.capgemini.hms.scheduling.dto.TrainedInDTO dto = com.capgemini.hms.scheduling.dto.TrainedInDTO.builder()
                .physicianId(101)
                .procedureCode(1)
                .treatmentDate(LocalDateTime.now())
                .certificationExpires(LocalDateTime.now().plusDays(1))
                .build();
        com.capgemini.hms.patient.entity.Procedures procedure =
                com.capgemini.hms.patient.entity.Procedures.builder().code(1).name("Procedure").build();

        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        when(proceduresRepository.findByCodeAndIsDeletedFalse(1)).thenReturn(Optional.of(procedure));

        com.capgemini.hms.scheduling.dto.TrainedInDTO result = schedulingService.addCertification(dto);

        assertEquals(dto, result);
        verify(trainedInRepository).save(any(com.capgemini.hms.scheduling.entity.TrainedIn.class));
    }

    @Test
    void getAllAppointments_ShouldFilterDeletedAppointments() {
        Appointment active = Appointment.builder().appointmentId(1).patient(Patient.builder().ssn(10).build()).physician(physician).isDeleted(false).build();
        Appointment deleted = Appointment.builder().appointmentId(2).patient(Patient.builder().ssn(11).build()).physician(physician).isDeleted(true).build();
        when(appointmentRepository.findAll()).thenReturn(List.of(active, deleted));

        List<AppointmentDTO> result = schedulingService.getAllAppointments();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAppointmentId());
    }

    @Test
    void searchAppointments_ShouldMapResults() {
        Appointment appointment = Appointment.builder()
                .appointmentId(1)
                .patient(Patient.builder().ssn(10).build())
                .physician(physician)
                .examinationRoom("A")
                .build();
        when(appointmentRepository.searchByRoom("A")).thenReturn(List.of(appointment));

        List<AppointmentDTO> result = schedulingService.searchAppointments("A");

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getExaminationRoom());
    }

    @Test
    void updateAppointment_ShouldUpdateAndPersist() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        Appointment existing = Appointment.builder()
                .appointmentId(5001)
                .patient(Patient.builder().ssn(1001).build())
                .physician(physician)
                .isDeleted(false)
                .build();
        AppointmentDTO dto = AppointmentDTO.builder()
                .start(start)
                .end(end)
                .examinationRoom("Room A")
                .build();
        when(appointmentRepository.findByAppointmentIdAndIsDeletedFalse(5001)).thenReturn(Optional.of(existing));
        when(appointmentRepository.save(existing)).thenReturn(existing);

        AppointmentDTO result = schedulingService.updateAppointment(5001, dto);

        assertEquals(5001, result.getAppointmentId());
        assertEquals("Room A", result.getExaminationRoom());
    }

    @Test
    void bookAppointment_ShouldAllowMissingPrepNurse() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        AppointmentDTO dto = AppointmentDTO.builder()
                .appointmentId(5002)
                .patientSsn(1001)
                .physicianId(101)
                .start(start)
                .end(end)
                .examinationRoom("Room B")
                .build();
        Patient patient = Patient.builder().ssn(1001).isDeleted(false).build();

        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentDTO result = schedulingService.bookAppointment(dto);

        assertEquals(5002, result.getAppointmentId());
        assertNull(result.getPrepNurseId());
        verify(nurseRepository, never()).findByEmployeeIdAndIsDeletedFalse(any());
    }

    @Test
    void updateAppointment_ShouldRejectMissingTimes() {
        Appointment existing = Appointment.builder()
                .appointmentId(5001)
                .patient(Patient.builder().ssn(1001).build())
                .physician(physician)
                .isDeleted(false)
                .build();
        AppointmentDTO dto = AppointmentDTO.builder()
                .examinationRoom("Room A")
                .build();
        when(appointmentRepository.findByAppointmentIdAndIsDeletedFalse(5001)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> schedulingService.updateAppointment(5001, dto));
    }
}
