package com.capgemini.hms.infrastructure.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.infrastructure.dto.BlockDTO;
import com.capgemini.hms.infrastructure.dto.RoomDTO;
import com.capgemini.hms.infrastructure.service.InfrastructureService;
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
class InfrastructureControllerTest {

    @Mock
    private InfrastructureService infrastructureService;

    @InjectMocks
    private InfrastructureController infrastructureController;

    @Test
    void getAllRooms_shouldReturnList() {
        List<RoomDTO> rooms = List.of(RoomDTO.builder().roomNumber(101).build());
        when(infrastructureService.getAllRooms()).thenReturn(rooms);

        ResponseEntity<ApiResponse<List<RoomDTO>>> response = infrastructureController.getAllRooms();

        assertEquals(rooms, response.getBody().getData());
    }

    @Test
    void getAvailableRooms_shouldReturnList() {
        List<RoomDTO> rooms = List.of(RoomDTO.builder().roomNumber(101).build());
        when(infrastructureService.getAvailableRooms()).thenReturn(rooms);

        ResponseEntity<ApiResponse<List<RoomDTO>>> response = infrastructureController.getAvailableRooms();

        assertEquals(rooms, response.getBody().getData());
    }

    @Test
    void addRoom_shouldReturnCreated() {
        RoomDTO dto = RoomDTO.builder().roomNumber(101).build();
        when(infrastructureService.addRoom(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<RoomDTO>> response = infrastructureController.addRoom(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateRoom_shouldReturnUpdated() {
        RoomDTO dto = RoomDTO.builder().roomNumber(101).build();
        when(infrastructureService.updateRoom(101, dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<RoomDTO>> response = infrastructureController.updateRoom(101, dto);

        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void deleteRoom_shouldDelegate() {
        ResponseEntity<ApiResponse<String>> response = infrastructureController.deleteRoom(101);

        verify(infrastructureService).deleteRoom(101);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllBlocks_shouldReturnList() {
        List<BlockDTO> blocks = List.of(BlockDTO.builder().blockFloor(1).build());
        when(infrastructureService.getAllBlocks()).thenReturn(blocks);

        ResponseEntity<ApiResponse<List<BlockDTO>>> response = infrastructureController.getAllBlocks();

        assertEquals(blocks, response.getBody().getData());
    }
}
