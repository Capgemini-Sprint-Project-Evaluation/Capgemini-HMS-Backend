package com.capgemini.hms.nursing.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.nursing.dto.NurseDTO;
import com.capgemini.hms.nursing.dto.OnCallDTO;
import com.capgemini.hms.nursing.service.NursingService;
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
class NursingControllerTest {

    @Mock
    private NursingService nursingService;

    @InjectMocks
    private NursingController nursingController;

    @Test
    void getAllNurses_shouldReturnList() {
        List<NurseDTO> nurses = List.of(NurseDTO.builder().name("Nurse Joy").build());
        when(nursingService.getAllNurses()).thenReturn(nurses);

        ResponseEntity<ApiResponse<List<NurseDTO>>> response = nursingController.getAllNurses();

        assertEquals(nurses, response.getBody().getData());
    }

    @Test
    void searchNurses_shouldReturnList() {
        List<NurseDTO> nurses = List.of(NurseDTO.builder().name("Nurse Joy").build());
        when(nursingService.searchNurses("joy")).thenReturn(nurses);

        ResponseEntity<ApiResponse<List<NurseDTO>>> response = nursingController.searchNurses("joy");

        assertEquals(nurses, response.getBody().getData());
    }

    @Test
    void getNurseById_shouldReturnNurse() {
        NurseDTO nurse = NurseDTO.builder().employeeId(1).build();
        when(nursingService.getNurseById(1)).thenReturn(nurse);

        ResponseEntity<ApiResponse<NurseDTO>> response = nursingController.getNurseById(1);

        assertEquals(nurse, response.getBody().getData());
    }

    @Test
    void registerNurse_shouldReturnCreated() {
        NurseDTO nurse = NurseDTO.builder().employeeId(1).build();
        when(nursingService.registerNurse(nurse)).thenReturn(nurse);

        ResponseEntity<ApiResponse<NurseDTO>> response = nursingController.registerNurse(nurse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateNurse_shouldReturnUpdated() {
        NurseDTO nurse = NurseDTO.builder().employeeId(1).build();
        when(nursingService.updateNurse(1, nurse)).thenReturn(nurse);

        ResponseEntity<ApiResponse<NurseDTO>> response = nursingController.updateNurse(1, nurse);

        assertEquals(nurse, response.getBody().getData());
    }

    @Test
    void deleteNurse_shouldDelegate() {
        ResponseEntity<ApiResponse<String>> response = nursingController.deleteNurse(1);

        verify(nursingService).deleteNurse(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getOnCallRotation_shouldReturnList() {
        List<OnCallDTO> rotation = List.of(OnCallDTO.builder().nurseId(1).build());
        when(nursingService.getOnCallRotation()).thenReturn(rotation);

        ResponseEntity<ApiResponse<List<OnCallDTO>>> response = nursingController.getOnCallRotation();

        assertEquals(rotation, response.getBody().getData());
    }
}
