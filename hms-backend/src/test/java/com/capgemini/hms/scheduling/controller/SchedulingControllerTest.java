package com.capgemini.hms.scheduling.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.scheduling.dto.AppointmentDTO;
import com.capgemini.hms.scheduling.dto.PhysicianDTO;
import com.capgemini.hms.scheduling.dto.TrainedInDTO;
import com.capgemini.hms.scheduling.service.SchedulingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;

    @InjectMocks
    private SchedulingController schedulingController;

    @Test
    void schedulingEndpoints_shouldReturnExpectedResponses() {
        List<PhysicianDTO> physicians = List.of(PhysicianDTO.builder().employeeId(1).name("Dr").build());
        PhysicianDTO physician = PhysicianDTO.builder().employeeId(1).name("Dr").build();
        List<TrainedInDTO> certifications = List.of(TrainedInDTO.builder().physicianId(1).build());
        TrainedInDTO certification = TrainedInDTO.builder().physicianId(1).build();
        List<AppointmentDTO> appointments = List.of(AppointmentDTO.builder().appointmentId(1).build());
        AppointmentDTO appointment = AppointmentDTO.builder().appointmentId(1).build();

        when(schedulingService.getAllPhysicians()).thenReturn(physicians);
        when(schedulingService.searchPhysicians("dr")).thenReturn(physicians);
        when(schedulingService.getPhysicianById(1)).thenReturn(physician);
        when(schedulingService.addPhysician(physician)).thenReturn(physician);
        when(schedulingService.updatePhysician(1, physician)).thenReturn(physician);
        when(schedulingService.getCertificationsByPhysician(1)).thenReturn(certifications);
        when(schedulingService.addCertification(certification)).thenReturn(certification);
        when(schedulingService.getAllAppointments()).thenReturn(appointments);
        when(schedulingService.searchAppointments("A")).thenReturn(appointments);
        when(schedulingService.updateAppointment(1, appointment)).thenReturn(appointment);
        when(schedulingService.bookAppointment(appointment)).thenReturn(appointment);

        assertEquals(physicians, schedulingController.getAllPhysicians().getBody().getData());
        assertEquals(physicians, schedulingController.searchPhysicians("dr").getBody().getData());
        assertEquals(physician, schedulingController.getPhysicianById(1).getBody().getData());
        assertEquals(HttpStatus.CREATED, schedulingController.addPhysician(physician).getStatusCode());
        assertEquals(physician, schedulingController.updatePhysician(1, physician).getBody().getData());
        assertEquals(HttpStatus.OK, schedulingController.deletePhysician(1).getStatusCode());
        assertEquals(certifications, schedulingController.getCertifications(1).getBody().getData());
        assertEquals(HttpStatus.CREATED, schedulingController.addCertification(certification).getStatusCode());
        assertEquals(appointments, schedulingController.getAllAppointments().getBody().getData());
        assertEquals(appointments, schedulingController.searchAppointments("A").getBody().getData());
        assertEquals(appointment, schedulingController.updateAppointment(1, appointment).getBody().getData());
        assertEquals(HttpStatus.CREATED, schedulingController.bookAppointment(appointment).getStatusCode());
        assertEquals(HttpStatus.OK, schedulingController.cancelAppointment(1).getStatusCode());

        verify(schedulingService).deletePhysician(1);
        verify(schedulingService).cancelAppointment(1);
    }
}
