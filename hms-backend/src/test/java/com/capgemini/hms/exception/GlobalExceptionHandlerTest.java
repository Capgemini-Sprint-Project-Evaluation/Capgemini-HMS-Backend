package com.capgemini.hms.exception;

import com.capgemini.hms.common.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.core.MethodParameter;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "username", "Username is required."));
        Method method = ValidationTarget.class.getDeclaredMethod("submit", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        methodArgumentNotValidException = new MethodArgumentNotValidException(methodParameter, bindingResult);
    }

    @Test
    void handleUnreadableBody_shouldReturnConsistentBadRequestResponse() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleUnreadableBody(new HttpMessageNotReadableException("bad body"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Malformed request body.", response.getBody().getMessage());
    }

    @Test
    void handleValidationExceptions_shouldReturnFieldErrors() {
        ResponseEntity<ApiResponse<java.util.Map<String, Object>>> response =
                handler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("Username is required.", response.getBody().getErrors().get("username"));
    }

    @Test
    void handleConstraintViolation_shouldReturnValidationErrorMap() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("patient.ssn");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be positive");

        ResponseEntity<ApiResponse<java.util.Map<String, Object>>> response =
                handler.handleConstraintViolation(new ConstraintViolationException(Set.of(violation)));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("must be positive", response.getBody().getErrors().get("patient.ssn"));
    }

    @Test
    void handleDataIntegrity_shouldReturnForeignKeyConflictMessage() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleDataIntegrity(new DataIntegrityViolationException("foreign key violation"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                "Operation failed: A referenced record (like Physician ID or Department ID) does not exist in the system.",
                response.getBody().getMessage()
        );
    }

    @Test
    void handleNoResourceFound_shouldIncludeRequestedPath() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleNoResourceFound(new NoResourceFoundException(org.springframework.http.HttpMethod.GET, "/missing"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found: /missing", response.getBody().getMessage());
    }

    @Test
    void handleDataIntegrity_shouldReturnDuplicateEntryMessage() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleDataIntegrity(new DataIntegrityViolationException("Duplicate entry"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                "Operation failed: Records with these unique details already exist.",
                response.getBody().getMessage()
        );
    }

    @Test
    void handleDataIntegrity_shouldReturnGenericIntegrityMessageWhenNull() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleDataIntegrity(new DataIntegrityViolationException(null));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                "Database integrity error. This often happens when referencing an ID that doesn't exist (like an invalid HeadId) or violating a unique constraint.",
                response.getBody().getMessage()
        );
    }

    @Test
    void handleAccessDenied_shouldReturnForbidden() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleAccessDenied(new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You do not have permission to perform this operation.", response.getBody().getMessage());
    }

    @Test
    void handleMethodNotSupported_shouldReturnMethodNotAllowed() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleMethodNotSupported(new HttpRequestMethodNotSupportedException("TRACE"));

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("HTTP method not supported for this endpoint.", response.getBody().getMessage());
    }

    @Test
    void handleEntityNotFound_shouldReturnNotFound() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleEntityNotFound(new EntityNotFoundException("department missing"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Requested resource not found: department missing", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgument_shouldReturnBadRequest() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleIllegalArgument(new IllegalArgumentException("bad arg"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("bad arg", response.getBody().getMessage());
    }

    @Test
    void handleDataAccess_shouldReturnServiceUnavailable() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleDataAccess(new DataAccessResourceFailureException("db down"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("A database error occurred. Please check your connection or data format.", response.getBody().getMessage());
    }

    @Test
    void handleBadRequest_shouldReturnBadRequestStatus() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleBadRequest(new BadRequestException("bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("bad request", response.getBody().getMessage());
    }

    @Test
    void handleNotFound_shouldReturnNotFoundStatus() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleNotFound(new ResourceNotFoundException("missing resource"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("missing resource", response.getBody().getMessage());
    }

    @Test
    void handleConflict_shouldReturnConflictStatus() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleConflict(new ConflictException("conflict"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("conflict", response.getBody().getMessage());
    }

    @Test
    void handleAllExceptions_shouldReturnInternalServerError() {
        ResponseEntity<ApiResponse<String>> response =
                handler.handleAllExceptions(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(
                "An unexpected internal server error occurred. Check backend logs for correlation ID. Detail: RuntimeException",
                response.getBody().getMessage()
        );
    }

    private static final class ValidationTarget {
        @SuppressWarnings("unused")
        private void submit(String username) {
        }
    }
}
