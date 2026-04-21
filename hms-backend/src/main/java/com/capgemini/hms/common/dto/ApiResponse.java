package com.capgemini.hms.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Map<String, Object> errors;
    private Instant timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(String status, String message, T data, Map<String, Object> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = Instant.now();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public Map<String, Object> getErrors() { return errors; }
    public void setErrors(Map<String, Object> errors) { this.errors = errors; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("SUCCESS", message, data, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null, null);
    }

    public static <T> ApiResponse<T> error(String message, Map<String, Object> errors) {
        return new ApiResponse<>("ERROR", message, null, errors);
    }
}
