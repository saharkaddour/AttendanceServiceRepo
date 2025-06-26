package com.education.attendance.exception;

import lombok.Data; // Assuming Lombok is configured and working
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error; // E.g., "Bad Request", "Not Found", "Internal Server Error"
    private String message; // A concise error message
    private String path;    // The request URI that caused the error
    private Map<String, String> fieldErrors; // Specific for validation errors (field -> message)

    // Constructor for general errors
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Constructor for validation errors (includes fieldErrors)
    public ErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        this(status, error, message, path);
        this.fieldErrors = fieldErrors;
    }
}