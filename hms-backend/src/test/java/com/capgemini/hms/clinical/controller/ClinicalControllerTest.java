package com.capgemini.hms.clinical.controller;

import com.capgemini.hms.clinical.dto.MedicationDTO;
import com.capgemini.hms.clinical.dto.PrescriptionDTO;
import com.capgemini.hms.clinical.service.ClinicalService;
import com.capgemini.hms.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicalControllerTest {

    @Mock
    private ClinicalService clinicalService;

    @InjectMocks
    private ClinicalController clinicalController;

    @Test
    void getAllMedications_shouldReturnList() {
        List<MedicationDTO> medications = List.of(MedicationDTO.builder().name("Aspirin").build());
        when(clinicalService.getAllMedications()).thenReturn(medications);

        ResponseEntity<ApiResponse<List<MedicationDTO>>> response = clinicalController.getAllMedications();

        assertEquals(medications, response.getBody().getData());
    }

    @Test
    void searchMedications_shouldReturnList() {
        List<MedicationDTO> medications = List.of(MedicationDTO.builder().name("Aspirin").build());
        when(clinicalService.searchMedications("asp")).thenReturn(medications);

        ResponseEntity<ApiResponse<List<MedicationDTO>>> response = clinicalController.searchMedications("asp");

        assertEquals(medications, response.getBody().getData());
    }

    @Test
    void getMedicationByCode_shouldReturnMedication() {
        MedicationDTO dto = MedicationDTO.builder().code(1).name("Aspirin").build();
        when(clinicalService.getMedicationByCode(1)).thenReturn(dto);

        ResponseEntity<ApiResponse<MedicationDTO>> response = clinicalController.getMedicationByCode(1);

        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void addMedication_shouldReturnCreated() {
        MedicationDTO dto = MedicationDTO.builder().code(1).name("Aspirin").build();
        when(clinicalService.addMedication(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<MedicationDTO>> response = clinicalController.addMedication(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateMedication_shouldReturnUpdated() {
        MedicationDTO dto = MedicationDTO.builder().code(1).name("Aspirin").build();
        when(clinicalService.updateMedication(1, dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<MedicationDTO>> response = clinicalController.updateMedication(1, dto);

        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void deleteMedication_shouldDelegate() {
        ResponseEntity<ApiResponse<String>> response = clinicalController.deleteMedication(1);

        verify(clinicalService).deleteMedication(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void prescribe_shouldReturnCreated() {
        PrescriptionDTO dto = PrescriptionDTO.builder().patientSsn(1).build();
        when(clinicalService.prescribe(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<PrescriptionDTO>> response = clinicalController.prescribe(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getPrescriptionsByPatient_shouldReturnList() {
        List<PrescriptionDTO> prescriptions = List.of(PrescriptionDTO.builder().patientSsn(1).build());
        when(clinicalService.getPrescriptionsByPatient(1)).thenReturn(prescriptions);

        ResponseEntity<ApiResponse<List<PrescriptionDTO>>> response = clinicalController.getPrescriptionsByPatient(1);

        assertEquals(prescriptions, response.getBody().getData());
    }
}
