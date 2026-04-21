package com.capgemini.hms.infrastructure.controller;

import com.capgemini.hms.common.dto.ApiResponse;
import com.capgemini.hms.common.dto.ApiResponses;
import com.capgemini.hms.infrastructure.dto.RoomDTO;
import com.capgemini.hms.infrastructure.service.InfrastructureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/infrastructure")
@RequiredArgsConstructor
public class InfrastructureController {

    private final InfrastructureService infrastructureService;

    @GetMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAllRooms() {
        return ApiResponses.ok(infrastructureService.getAllRooms());
    }

    @GetMapping("/rooms/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAvailableRooms() {
        return ApiResponses.ok(infrastructureService.getAvailableRooms());
    }

    @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomDTO>> addRoom(@Valid @RequestBody RoomDTO roomDTO) {
        return ApiResponses.created("Room added successfully.", infrastructureService.addRoom(roomDTO));
    }

    @PutMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomDTO>> updateRoom(@PathVariable Integer id, @Valid @RequestBody RoomDTO roomDTO) {
        return ApiResponses.ok("Room details updated successfully.", infrastructureService.updateRoom(id, roomDTO));
    }

    @DeleteMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable Integer id) {
        infrastructureService.deleteRoom(id);
        return ApiResponses.ok("Room successfully soft-deleted.", null);
    }

    // --- BLOCKS ---

    @GetMapping("/blocks")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<List<com.capgemini.hms.infrastructure.dto.BlockDTO>>> getAllBlocks() {
        return ApiResponses.ok(infrastructureService.getAllBlocks());
    }
}
