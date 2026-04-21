package com.capgemini.hms.nursing.service;

import com.capgemini.hms.nursing.dto.NurseDTO;
import com.capgemini.hms.nursing.dto.OnCallDTO;
import com.capgemini.hms.nursing.entity.Nurse;
import com.capgemini.hms.nursing.entity.OnCall;
import com.capgemini.hms.nursing.entity.OnCallId;
import com.capgemini.hms.nursing.repository.NurseRepository;
import com.capgemini.hms.nursing.repository.OnCallRepository;
import com.capgemini.hms.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NursingServiceTest {

    @Mock
    private NurseRepository nurseRepository;

    @Mock
    private OnCallRepository onCallRepository;

    @InjectMocks
    private NursingService nursingService;

    private Nurse nurse;
    private NurseDTO nurseDTO;

    @BeforeEach
    void setUp() {
        nurse = Nurse.builder()
                .employeeId(101)
                .name("Nurse Joy")
                .position("Head Nurse")
                .registered(true)
                .ssn(111222)
                .isDeleted(false)
                .build();

        nurseDTO = NurseDTO.builder()
                .employeeId(101)
                .name("Nurse Joy")
                .position("Head Nurse")
                .registered(true)
                .ssn(111222)
                .build();
    }

    @Test
    void getAllNurses_ShouldReturnList() {
        when(nurseRepository.findAll()).thenReturn(Arrays.asList(nurse));
        List<NurseDTO> result = nursingService.getAllNurses();
        assertFalse(result.isEmpty());
        assertEquals("Nurse Joy", result.get(0).getName());
    }

    @Test
    void getNurseById_Success() {
        when(nurseRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(nurse));
        NurseDTO result = nursingService.getNurseById(101);
        assertNotNull(result);
        assertEquals(101, result.getEmployeeId());
    }

    @Test
    void getNurseById_NotFound() {
        when(nurseRepository.findByEmployeeIdAndIsDeletedFalse(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> nursingService.getNurseById(999));
    }

    @Test
    void registerNurse_Success() {
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);
        NurseDTO result = nursingService.registerNurse(nurseDTO);
        assertNotNull(result);
        verify(nurseRepository).save(any(Nurse.class));
    }

    @Test
    void deleteNurse_Success() {
        when(nurseRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(nurse));
        nursingService.deleteNurse(101);
        assertTrue(nurse.getIsDeleted());
        verify(nurseRepository).save(nurse);
    }

    @Test
    void searchNurses_ShouldReturnResults() {
        when(nurseRepository.searchActiveNurses("joy")).thenReturn(List.of(nurse));

        List<NurseDTO> result = nursingService.searchNurses("joy");

        assertEquals(1, result.size());
    }

    @Test
    void updateNurse_Success() {
        when(nurseRepository.findByEmployeeIdAndIsDeletedFalse(101)).thenReturn(Optional.of(nurse));
        when(nurseRepository.save(nurse)).thenReturn(nurse);

        NurseDTO result = nursingService.updateNurse(101, nurseDTO);

        assertEquals(101, result.getEmployeeId());
    }

    @Test
    void getOnCallRotation_ShouldMapResults() {
        OnCall onCall = OnCall.builder()
                .id(new OnCallId(101, 2, 3, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusHours(1)))
                .nurse(nurse)
                .build();
        when(onCallRepository.findAll()).thenReturn(List.of(onCall));

        List<OnCallDTO> result = nursingService.getOnCallRotation();

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getNurseId());
    }
}
