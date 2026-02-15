package com.bharat.airport.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global exception handler for consistent error responses */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("Business rule violation: {}", ex.getMessage());

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Business Rule Violation",
            ex.getMessage(),
            LocalDateTime.now());

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Invalid input data: " + errors.toString(),
            LocalDateTime.now());

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("Unexpected error occurred", ex);

    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred",
            LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  /** Error response structure */
  public static class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
      this.status = status;
      this.error = error;
      this.message = message;
      this.timestamp = timestamp;
    }

    // Getters
    public int getStatus() {
      return status;
    }

    public String getError() {
      return error;
    }

    public String getMessage() {
      return message;
    }

    public LocalDateTime getTimestamp() {
      return timestamp;
    }
  }
}
