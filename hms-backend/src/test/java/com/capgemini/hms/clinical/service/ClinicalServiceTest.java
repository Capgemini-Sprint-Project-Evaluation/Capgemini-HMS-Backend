package com.capgemini.hms.clinical.service;

import com.capgemini.hms.clinical.dto.MedicationDTO;
import com.capgemini.hms.clinical.dto.PrescriptionDTO;
import com.capgemini.hms.clinical.entity.Medication;
import com.capgemini.hms.clinical.entity.Prescription;
import com.capgemini.hms.clinical.repository.MedicationRepository;
import com.capgemini.hms.clinical.repository.PrescriptionRepository;
import com.capgemini.hms.exception.BadRequestException;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.patient.entity.Patient;
import com.capgemini.hms.patient.repository.PatientRepository;
import com.capgemini.hms.scheduling.entity.Physician;
import com.capgemini.hms.scheduling.repository.PhysicianRepository;
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
class ClinicalServiceTest {

    @Mock private MedicationRepository medicationRepository;
    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private PhysicianRepository physicianRepository;
    @Mock private PatientRepository patientRepository;

    @InjectMocks
    private ClinicalService clinicalService;

    private Medication medication;
    private MedicationDTO medicationDTO;

    @BeforeEach
    void setUp() {
        medication = Medication.builder()
                .code(1)
                .name("Aspirin")
                .brand("Bayer")
                .isDeleted(false)
                .build();

        medicationDTO = MedicationDTO.builder()
                .code(1)
                .name("Aspirin")
                .brand("Bayer")
                .build();
    }

    @Test
    void getAllMedications_ShouldReturnList() {
        when(medicationRepository.findAll()).thenReturn(Arrays.asList(medication));
        List<MedicationDTO> result = clinicalService.getAllMedications();
        assertFalse(result.isEmpty());
        assertEquals("Aspirin", result.get(0).getName());
    }

    @Test
    void getMedicationByCode_Success() {
        when(medicationRepository.findByCodeAndIsDeletedFalse(1)).thenReturn(Optional.of(medication));
        MedicationDTO result = clinicalService.getMedicationByCode(1);
        assertNotNull(result);
        assertEquals(1, result.getCode());
    }

    @Test
    void getMedicationByCode_NotFound() {
        when(medicationRepository.findByCodeAndIsDeletedFalse(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clinicalService.getMedicationByCode(99));
    }

    @Test
    void addMedication_Success() {
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);
        MedicationDTO result = clinicalService.addMedication(medicationDTO);
        assertNotNull(result);
        verify(medicationRepository).save(any(Medication.class));
    }

    @Test
    void prescribe_Success() {
        PrescriptionDTO pDto = PrescriptionDTO.builder()
                .physicianId(101)
                .patientSsn(202)
                .medicationCode(1)
                .date(LocalDateTime.now())
                .dose("500mg")
                .build();

        Physician physician = Physician.builder().employeeId(101).name("Dr. House").build();
        Patient patient = Patient.builder().ssn(202).name("John Doe").build();
        
        when(physicianRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(physician));
        when(patientRepository.findBySsnAndIsDeletedFalse(202)).thenReturn(Optional.of(patient));
        when(medicationRepository.findByCodeAndIsDeletedFalse(1)).thenReturn(Optional.of(medication));
        when(prescriptionRepository.save(any(Prescription.class))).thenAnswer(i -> i.getArguments()[0]);

        PrescriptionDTO result = clinicalService.prescribe(pDto);

        assertNotNull(result);
        assertEquals(101, result.getPhysicianId());
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    void searchMedications_ShouldReturnResults() {
        when(medicationRepository.searchActiveMedications("asp")).thenReturn(List.of(medication));

        List<MedicationDTO> result = clinicalService.searchMedications("asp");

        assertEquals(1, result.size());
    }

    @Test
    void updateMedication_Success() {
        when(medicationRepository.findByCodeAndIsDeletedFalse(1)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(medication)).thenReturn(medication);

        MedicationDTO result = clinicalService.updateMedication(1, medicationDTO);

        assertEquals(1, result.getCode());
    }

    @Test
    void deleteMedication_Success() {
        when(medicationRepository.findByCodeAndIsDeletedFalse(1)).thenReturn(Optional.of(medication));

        clinicalService.deleteMedication(1);

        assertTrue(medication.getIsDeleted());
        verify(medicationRepository).save(medication);
    }

    @Test
    void prescribe_ShouldRejectMissingDate() {
        PrescriptionDTO pDto = PrescriptionDTO.builder()
                .physicianId(101)
                .patientSsn(202)
                .medicationCode(1)
                .build();

        assertThrows(BadRequestException.class, () -> clinicalService.prescribe(pDto));
    }

    @Test
    void getPrescriptionsByPatient_ShouldReturnMappedResults() {
        Physician physician = Physician.builder().employeeId(101).build();
        Patient patient = Patient.builder().ssn(202).build();
        Prescription prescription = Prescription.builder()
                .physician(physician)
                .patient(patient)
                .medication(medication)
                .id(new com.capgemini.hms.clinical.entity.PrescriptionId(101, 202, 1, LocalDateTime.now()))
                .dose("500mg")
                .appointmentId(44)
                .build();
        when(prescriptionRepository.findByIdPatient(202)).thenReturn(List.of(prescription));

        List<PrescriptionDTO> result = clinicalService.getPrescriptionsByPatient(202);

        assertEquals(1, result.size());
        assertEquals(44, result.get(0).getAppointmentId());
    }
}
