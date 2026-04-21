package com.capgemini.hms.patient.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.patient.dto.PatientDTO;
import com.capgemini.hms.patient.dto.ProcedureDTO;
import com.capgemini.hms.patient.dto.StayDTO;
import com.capgemini.hms.patient.dto.UndergoesDTO;
import com.capgemini.hms.patient.service.PatientService;
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
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Test
    void patientEndpoints_shouldReturnExpectedResponses() {
        List<PatientDTO> patients = List.of(PatientDTO.builder().name("John").build());
        PatientDTO patient = PatientDTO.builder().ssn(1).name("John").build();
        List<StayDTO> stays = List.of(StayDTO.builder().stayId(1).build());
        StayDTO stay = StayDTO.builder().stayId(1).build();
        List<ProcedureDTO> procedures = List.of(ProcedureDTO.builder().name("Procedure").build());
        ProcedureDTO procedure = ProcedureDTO.builder().name("Procedure").build();
        List<UndergoesDTO> undergoes = List.of(UndergoesDTO.builder().patientSsn(1).build());
        UndergoesDTO undergoesDTO = UndergoesDTO.builder().patientSsn(1).build();

        when(patientService.getAllPatients()).thenReturn(patients);
        when(patientService.searchPatients("john")).thenReturn(patients);
        when(patientService.getPatientBySsn(1)).thenReturn(patient);
        when(patientService.getStayHistory(1)).thenReturn(stays);
        when(patientService.getActiveStays()).thenReturn(stays);
        when(patientService.updateStay(1, stay)).thenReturn(stay);
        when(patientService.savePatient(patient)).thenReturn(patient);
        when(patientService.updatePatient(1, patient)).thenReturn(patient);
        when(patientService.getAllProcedures()).thenReturn(procedures);
        when(patientService.searchProcedures("proc")).thenReturn(procedures);
        when(patientService.getProceduresByPatient(1)).thenReturn(procedures);
        when(patientService.addProcedure(procedure)).thenReturn(procedure);
        when(patientService.getUndergoesHistory(1)).thenReturn(undergoes);
        when(patientService.recordProcedure(undergoesDTO)).thenReturn(undergoesDTO);

        assertEquals(patients, patientController.getAllPatients().getBody().getData());
        assertEquals(patients, patientController.searchPatients("john").getBody().getData());
        assertEquals(patient, patientController.getPatientBySsn(1).getBody().getData());
        assertEquals(stays, patientController.getStayHistory(1).getBody().getData());
        assertEquals(stays, patientController.getActiveStays().getBody().getData());
        assertEquals(stay, patientController.updateStay(1, stay).getBody().getData());
        assertEquals(HttpStatus.CREATED, patientController.savePatient(patient).getStatusCode());
        assertEquals(patient, patientController.updatePatient(1, patient).getBody().getData());
        assertEquals(HttpStatus.OK, patientController.deletePatient(1).getStatusCode());
        assertEquals(procedures, patientController.getAllProcedures().getBody().getData());
        assertEquals(procedures, patientController.searchProcedures("proc").getBody().getData());
        assertEquals(procedures, patientController.getPatientProcedures(1).getBody().getData());
        assertEquals(HttpStatus.CREATED, patientController.addProcedure(procedure).getStatusCode());
        assertEquals(undergoes, patientController.getUndergoesHistory(1).getBody().getData());
        assertEquals(HttpStatus.CREATED, patientController.recordProcedure(undergoesDTO).getStatusCode());

        verify(patientService).deletePatient(1);
    }
}
