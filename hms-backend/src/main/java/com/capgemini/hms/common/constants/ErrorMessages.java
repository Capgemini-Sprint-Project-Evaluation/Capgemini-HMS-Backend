package com.capgemini.hms.common.constants;

public final class ErrorMessages {
    public static final String APPOINTMENT_NOT_FOUND = "Appointment with ID %s was not found.";
    public static final String BLOCK_NOT_FOUND = "Block floor %s code %s was not found.";
    public static final String DATABASE_ACCESS_ERROR =
            "A database error occurred. Please check your connection or data format.";
    public static final String DATABASE_DUPLICATE_ENTRY =
            "Operation failed: Records with these unique details already exist.";
    public static final String DATABASE_FOREIGN_KEY_ERROR =
            "Operation failed: A referenced record (like Physician ID or Department ID) does not exist in the system.";
    public static final String DATABASE_INTEGRITY_ERROR =
            "Database integrity error. This often happens when referencing an ID that doesn't exist (like an invalid HeadId) or violating a unique constraint.";
    public static final String DEPARTMENT_ALREADY_EXISTS = "Department with ID %s already exists.";
    public static final String DEPARTMENT_NOT_FOUND = "Department with ID %s was not found.";
    public static final String INVALID_CREDENTIALS = "Incorrect username or password.";
    public static final String HTTP_METHOD_NOT_SUPPORTED = "HTTP method not supported for this endpoint.";
    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected internal server error occurred. Check backend logs for correlation ID. Detail: %s";
    public static final String MALFORMED_REQUEST_BODY = "Malformed request body.";
    public static final String MEDICATION_NOT_FOUND = "Medication with code %s was not found.";
    public static final String NURSE_NOT_FOUND = "Nurse with ID %s was not found.";
    public static final String PATIENT_NOT_FOUND = "Patient with SSN %s was not found.";
    public static final String PERMISSION_DENIED = "You do not have permission to perform this operation.";
    public static final String PHYSICIAN_NOT_FOUND = "Physician with ID %s was not found.";
    public static final String PROCEDURE_NOT_FOUND = "Procedure with code %s was not found.";
    public static final String REQUESTED_RESOURCE_NOT_FOUND = "Requested resource not found: %s";
    public static final String RESOURCE_NOT_FOUND = "Resource not found: %s";
    public static final String ROOM_NOT_FOUND = "Room %s was not found.";
    public static final String STAY_NOT_FOUND = "Stay with ID %s was not found.";
    public static final String VALIDATION_FAILED = "Validation failed";

    private ErrorMessages() {
    }
}
