package com.capgemini.hms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * Standard DTO for capturing structured error responses from the backend.
 * This matches the backend's ApiResponse structure but is tailored for error scenarios.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO implements Serializable {
    private String status;
    private String message;
    private Map<String, String> errors;
    private Object data;
    private String timestamp;

    public ErrorResponseDTO() {
        this.status = "ERROR";
    }

    public ErrorResponseDTO(String status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    /**
     * Helper to create a generic communication failure response.
     */
    public static ErrorResponseDTO communicationError(String detail) {
        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.setStatus("ERROR");
        dto.setMessage("Backend communication failed: " + detail);
        return dto;
    }
}
