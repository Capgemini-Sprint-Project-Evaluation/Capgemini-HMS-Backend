package com.capgemini.hms.infrastructure.service;

import com.capgemini.hms.common.constants.ErrorMessages;
import com.capgemini.hms.infrastructure.dto.RoomDTO;
import com.capgemini.hms.infrastructure.entity.Block;
import com.capgemini.hms.infrastructure.entity.BlockId;
import com.capgemini.hms.infrastructure.entity.Room;
import com.capgemini.hms.exception.ResourceNotFoundException;
import com.capgemini.hms.infrastructure.repository.BlockRepository;
import com.capgemini.hms.infrastructure.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfrastructureService {
    private final RoomRepository roomRepository;
    private final BlockRepository blockRepository;


    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> !room.getIsDeleted())
                .map(this::convertToDTO)
                .toList();
    }

    public List<RoomDTO> getAvailableRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> !room.getIsDeleted() && !room.getUnavailable())
                .map(this::convertToDTO)
                .toList();
    }

    public List<com.capgemini.hms.infrastructure.dto.BlockDTO> getAllBlocks() {
        return blockRepository.findAll().stream()
                .map(b -> com.capgemini.hms.infrastructure.dto.BlockDTO.builder()
                        .blockFloor(b.getId().getBlockfloor())
                        .blockCode(b.getId().getBlockcode())
                        .build())
                .toList();
    }

    @Transactional
    public RoomDTO addRoom(RoomDTO dto) {
        BlockId blockId = new BlockId(dto.getBlockFloor(), dto.getBlockCode());
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.BLOCK_NOT_FOUND.formatted(dto.getBlockFloor(), dto.getBlockCode())));

        Room room = Room.builder()
                .roomNumber(dto.getRoomNumber())
                .roomType(dto.getRoomType())
                .block(block)
                .unavailable(dto.getUnavailable())
                .build();

        return convertToDTO(roomRepository.save(room));
    }

    @Transactional
    public RoomDTO updateRoom(Integer roomNumber, RoomDTO dto) {
        Room existing = roomRepository.findByRoomNumberAndIsDeletedFalse(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND.formatted(roomNumber)));
        
        existing.setRoomType(dto.getRoomType());
        existing.setUnavailable(dto.getUnavailable());
        
        BlockId blockId = new BlockId(dto.getBlockFloor(), dto.getBlockCode());
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.BLOCK_NOT_FOUND.formatted(dto.getBlockFloor(), dto.getBlockCode())));
        existing.setBlock(block);

        return convertToDTO(roomRepository.save(existing));
    }

    @Transactional
    public void deleteRoom(Integer roomNumber) {
        Room room = roomRepository.findByRoomNumberAndIsDeletedFalse(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND.formatted(roomNumber)));
        room.setIsDeleted(true);
        roomRepository.save(room);
    }

    private RoomDTO convertToDTO(Room room) {
        return RoomDTO.builder()
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .blockFloor(room.getBlock().getId().getBlockfloor())
                .blockCode(room.getBlock().getId().getBlockcode())
                .unavailable(room.getUnavailable())
                .build();
    }
}
