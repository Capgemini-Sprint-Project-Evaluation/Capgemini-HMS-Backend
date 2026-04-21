package com.capgemini.hms.infrastructure.service;

import com.capgemini.hms.infrastructure.dto.RoomDTO;
import com.capgemini.hms.infrastructure.entity.Block;
import com.capgemini.hms.infrastructure.entity.BlockId;
import com.capgemini.hms.infrastructure.entity.Room;
import com.capgemini.hms.infrastructure.repository.BlockRepository;
import com.capgemini.hms.infrastructure.repository.RoomRepository;
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
class InfrastructureServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private InfrastructureService infrastructureService;

    private Room room;
    private Block block;
    private BlockId blockId;

    @BeforeEach
    void setUp() {
        blockId = new BlockId(1, 1);
        block = new Block(blockId);
        room = Room.builder()
                .roomNumber(101)
                .roomType("Single")
                .block(block)
                .unavailable(false)
                .isDeleted(false)
                .build();
    }

    @Test
    void getAllRooms_ShouldReturnList() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));

        List<RoomDTO> result = infrastructureService.getAllRooms();

        assertFalse(result.isEmpty());
        assertEquals(101, result.get(0).getRoomNumber());
    }

    @Test
    void getAvailableRooms_ShouldReturnOnlyAvailable() {
        Room unavailableRoom = Room.builder().roomNumber(102).unavailable(true).isDeleted(false).block(block).build();
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room, unavailableRoom));

        List<RoomDTO> result = infrastructureService.getAvailableRooms();

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
    }

    @Test
    void addRoom_Success() {
        RoomDTO dto = RoomDTO.builder()
                .roomNumber(101)
                .roomType("Single")
                .blockFloor(1)
                .blockCode(1)
                .unavailable(false)
                .build();

        when(blockRepository.findById(any(BlockId.class))).thenReturn(Optional.of(block));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomDTO result = infrastructureService.addRoom(dto);

        assertNotNull(result);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void addRoom_BlockNotFound() {
        RoomDTO dto = RoomDTO.builder().blockFloor(99).blockCode(99).build();
        when(blockRepository.findById(any(BlockId.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> infrastructureService.addRoom(dto));
    }

    @Test
    void deleteRoom_Success() {
        when(roomRepository.findByRoomNumberAndIsDeletedFalse(101)).thenReturn(Optional.of(room));

        infrastructureService.deleteRoom(101);

        assertTrue(room.getIsDeleted());
        verify(roomRepository).save(room);
    }

    @Test
    void updateRoom_Success() {
        RoomDTO dto = RoomDTO.builder()
                .roomNumber(101)
                .roomType("ICU")
                .blockFloor(1)
                .blockCode(1)
                .unavailable(true)
                .build();
        when(roomRepository.findByRoomNumberAndIsDeletedFalse(101)).thenReturn(Optional.of(room));
        when(blockRepository.findById(any(BlockId.class))).thenReturn(Optional.of(block));
        when(roomRepository.save(room)).thenReturn(room);

        RoomDTO result = infrastructureService.updateRoom(101, dto);

        assertNotNull(result);
        assertEquals(101, result.getRoomNumber());
    }

    @Test
    void getAllBlocks_ShouldMapBlocks() {
        when(blockRepository.findAll()).thenReturn(List.of(block));

        List<com.capgemini.hms.infrastructure.dto.BlockDTO> result = infrastructureService.getAllBlocks();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBlockFloor());
    }

    @Test
    void getAllRooms_ShouldFilterDeletedRooms() {
        Room deletedRoom = Room.builder()
                .roomNumber(102)
                .roomType("Standard")
                .block(block)
                .unavailable(false)
                .isDeleted(true)
                .build();
        when(roomRepository.findAll()).thenReturn(List.of(room, deletedRoom));

        List<RoomDTO> result = infrastructureService.getAllRooms();

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
    }
}
