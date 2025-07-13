package com.education.attendance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // For validation errors
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice; // Combination of @ControllerAdvice and @ResponseBody
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException; // Example for resource not found

@RestControllerAdvice // This annotation combines @ControllerAdvice and @ResponseBody
public class GlobalExceptionHandler {

    // --- Handler for Validation Errors (@Valid) ---
    // This method handles MethodArgumentNotValidException, which is thrown when @Valid fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        // Collect all field errors
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Validation failed for one or more fields",
                request.getDescription(false).replace("uri=", ""), // Extracts the request path
                fieldErrors // Include the detailed field errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // --- Handler for Resource Not Found ---
    // This handler can be used if your service methods throw NoSuchElementException
    // (currently, your controller handles 404 with Optional.orElse, which is also fine)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(), // Use the exception's message
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // --- Generic Handler for All Other Exceptions ---
    // This is a catch-all for any other unhandled exceptions.
    // It's important to provide a generic error message to avoid exposing sensitive details.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.", // Generic message for clients
                request.getDescription(false).replace("uri=", "")
        );
        // Log the actual exception for debugging purposes (this would go to your logger)
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace(); // For development, remove in production

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}