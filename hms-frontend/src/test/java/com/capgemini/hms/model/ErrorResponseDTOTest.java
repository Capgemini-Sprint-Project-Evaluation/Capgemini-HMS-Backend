package com.capgemini.hms.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorResponseDTOTest {

    @Test
    void defaultConstructor_shouldSetErrorStatus() {
        ErrorResponseDTO dto = new ErrorResponseDTO();

        assertEquals("ERROR", dto.getStatus());
        assertNull(dto.getMessage());
    }

    @Test
    void allArgsConstructorAndAccessors_shouldPopulateFields() {
        ErrorResponseDTO dto = new ErrorResponseDTO("ERROR", "bad request", Map.of("field", "required"));
        dto.setData(Map.of("code", 400));
        dto.setTimestamp("2026-04-20T10:00:00Z");

        assertEquals("ERROR", dto.getStatus());
        assertEquals("bad request", dto.getMessage());
        assertEquals("required", dto.getErrors().get("field"));
        assertEquals(Map.of("code", 400), dto.getData());
        assertEquals("2026-04-20T10:00:00Z", dto.getTimestamp());
    }

    @Test
    void communicationError_shouldCreateStandardFailureMessage() {
        ErrorResponseDTO dto = ErrorResponseDTO.communicationError("timeout");

        assertEquals("ERROR", dto.getStatus());
        assertEquals("Backend communication failed: timeout", dto.getMessage());
    }
}
