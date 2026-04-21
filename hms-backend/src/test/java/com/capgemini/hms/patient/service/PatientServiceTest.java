package com.capgemini.hms.patient.service;

import com.capgemini.hms.patient.dto.PatientDTO;
import com.capgemini.hms.patient.dto.ProcedureDTO;
import com.capgemini.hms.patient.dto.UndergoesDTO;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.entity.Procedures;
import com.capgemini.hms.patient.entity.Stay;
import com.capgemini.hms.patient.entity.Undergoes;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.patient.repository.ProceduresRepository;
import com.capgemini.hms.patient.repository.StayRepository;
import com.capgemini.hms.patient.repository.UndergoesRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepository patientRepository;
    @Mock private PhysicianRepository physicianRepository;
    @Mock private ProceduresRepository proceduresRepository;
    @Mock private UndergoesRepository undergoesRepository;
    @Mock private StayRepository stayRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .ssn(1001)
                .name("John Smith")
                .address("123 Maple St")
                .isDeleted(false)
                .build();

        patientDTO = PatientDTO.builder()
                .ssn(1001)
                .name("John Smith")
                .address("123 Maple St")
                .build();
    }

    @Test
    void getAllPatients_ShouldReturnList() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));
        List<PatientDTO> result = patientService.getAllPatients();
        assertFalse(result.isEmpty());
        assertEquals("John Smith", result.get(0).getName());
    }

    @Test
    void getPatientBySsn_Success() {
        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        PatientDTO result = patientService.getPatientBySsn(1001);
        assertNotNull(result);
        assertEquals(1001, result.getSsn());
    }

    @Test
    void savePatient_Success() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        PatientDTO result = patientService.savePatient(patientDTO);
        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void recordProcedure_Success() {
        LocalDateTime now = LocalDateTime.now();
        UndergoesDTO uDto = UndergoesDTO.builder()
                .patientSsn(1001)
                .procedureCode(50)
                .stayId(10)
                .physicianId(101)
                .date(now)
                .build();

        Procedures proc = Procedures.builder().code(50).name("Surgery").isDeleted(false).build();
        Stay stay = Stay.builder().stayId(10).patient(patient).isDeleted(false).build();
        Physician physician = Physician.builder().employeeId(101).isDeleted(false).build();

        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        when(proceduresRepository.findByCodeAndIsDeletedFalse(50)).thenReturn(Optional.of(proc));
        when(stayRepository.findByStayIdAndIsDeletedFalse(10)).thenReturn(Optional.of(stay));
        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        when(undergoesRepository.save(any(Undergoes.class))).thenAnswer(i -> i.getArguments()[0]);

        UndergoesDTO result = patientService.recordProcedure(uDto);

        assertNotNull(result);
        assertEquals(1001, result.getPatientSsn());
        verify(undergoesRepository).save(any(Undergoes.class));
    }

    @Test
    void recordProcedure_WrongStayPatient() {
        Patient otherPatient = Patient.builder().ssn(9999).build();
        Stay stay = Stay.builder().stayId(10).patient(otherPatient).isDeleted(false).build();
        
        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        when(proceduresRepository.findByCodeAndIsDeletedFalse(50)).thenReturn(Optional.of(Procedures.builder().code(50).build()));
        when(stayRepository.findByStayIdAndIsDeletedFalse(10)).thenReturn(Optional.of(stay));

        UndergoesDTO uDto = UndergoesDTO.builder().patientSsn(1001).procedureCode(50).stayId(10).date(LocalDateTime.now()).build();
        
        assertThrows(BadRequestException.class, () -> patientService.recordProcedure(uDto));
    }

    @Test
    void searchPatients_ShouldReturnMappedPatients() {
        when(patientRepository.searchActivePatients("john")).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.searchPatients("john");

        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());
    }

    @Test
    void getAllProcedures_ShouldFilterDeletedProcedures() {
        Procedures active = Procedures.builder().code(1).name("Active").cost(java.math.BigDecimal.ONE).isDeleted(false).build();
        Procedures deleted = Procedures.builder().code(2).name("Deleted").cost(java.math.BigDecimal.TEN).isDeleted(true).build();
        when(proceduresRepository.findAll()).thenReturn(List.of(active, deleted));

        List<ProcedureDTO> result = patientService.getAllProcedures();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getName());
    }

    @Test
    void searchProcedures_ShouldMatchByName() {
        Procedures active = Procedures.builder().code(1).name("Heart Surgery").cost(java.math.BigDecimal.ONE).isDeleted(false).build();
        when(proceduresRepository.findAll()).thenReturn(List.of(active));

        List<ProcedureDTO> result = patientService.searchProcedures("heart");

        assertEquals(1, result.size());
    }

    @Test
    void getProceduresByPatient_ShouldMapProcedureHistory() {
        Procedures procedure = Procedures.builder().code(50).name("Surgery").cost(java.math.BigDecimal.ONE).isDeleted(false).build();
        Undergoes undergoes = Undergoes.builder().procedure(procedure).build();
        when(undergoesRepository.findByPatientSsnAndIsDeletedFalse(1001)).thenReturn(List.of(undergoes));

        List<ProcedureDTO> result = patientService.getProceduresByPatient(1001);

        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getCode());
    }

    @Test
    void getStayHistory_ShouldReturnOnlyMatchingPatientStays() {
        Stay matching = Stay.builder().stayId(10).patient(patient).isDeleted(false).build();
        Stay other = Stay.builder().stayId(11).patient(Patient.builder().ssn(999).build()).isDeleted(false).build();
        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));
        when(stayRepository.findAll()).thenReturn(List.of(matching, other));

        List<com.capgemini.hms.patient.dto.StayDTO> result = patientService.getStayHistory(1001);

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getStayId());
    }

    @Test
    void getActiveStays_ShouldReturnMappedResults() {
        Stay active = Stay.builder().stayId(10).patient(patient).isDeleted(false).build();
        when(stayRepository.findByStayEndIsNullAndIsDeletedFalse()).thenReturn(List.of(active));

        List<com.capgemini.hms.patient.dto.StayDTO> result = patientService.getActiveStays();

        assertEquals(1, result.size());
    }

    @Test
    void updateStay_ShouldPersistChanges() {
        Stay existing = Stay.builder().stayId(10).patient(patient).isDeleted(false).build();
        com.capgemini.hms.patient.dto.StayDTO dto = com.capgemini.hms.patient.dto.StayDTO.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(1))
                .build();
        when(stayRepository.findByStayIdAndIsDeletedFalse(10)).thenReturn(Optional.of(existing));
        when(stayRepository.save(existing)).thenReturn(existing);

        com.capgemini.hms.patient.dto.StayDTO result = patientService.updateStay(10, dto);

        assertEquals(10, result.getStayId());
    }

    @Test
    void deletePatient_ShouldSoftDeletePatient() {
        when(patientRepository.findBySsnAndIsDeletedFalse(1001)).thenReturn(Optional.of(patient));

        patientService.deletePatient(1001);

        assertTrue(patient.getIsDeleted());
        verify(patientRepository).save(patient);
    }

    @Test
    void savePatient_ShouldAllowNullPrimaryCarePhysician() {
        patientDTO.setPcpId(null);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO result = patientService.savePatient(patientDTO);

        assertNotNull(result);
        verify(physicianRepository, never()).findByEmployeeIdAndIsDeletedFalse(any());
    }

    @Test
    void recordProcedure_ShouldRejectMissingDate() {
        UndergoesDTO dto = UndergoesDTO.builder()
                .patientSsn(1001)
                .procedureCode(50)
                .stayId(10)
                .physicianId(101)
                .build();

        assertThrows(BadRequestException.class, () -> patientService.recordProcedure(dto));
    }
}
