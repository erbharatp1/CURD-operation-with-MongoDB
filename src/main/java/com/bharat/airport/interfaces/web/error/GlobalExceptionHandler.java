package com.bharat.airport.interfaces.web.error;

import com.bharat.airport.config.ExcludeFromJacocoGeneratedReport;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ExcludeFromJacocoGeneratedReport
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<ErrorDetail> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorDetail("VALIDATION_ERROR", error.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(HandlerMethodValidationException ex) {
    List<ErrorDetail> errors =
        ex.getAllErrors().stream()
            .map(error -> new ErrorDetail("VALIDATION_ERROR", error.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingRequestBody(HttpMessageNotReadableException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("MALFORMED_REQUEST_BODY", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(InvalidRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidRequestException(InvalidRequestException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_DATA", ex.getErrorMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidRequestException(BadRequestException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_DATA", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGenericException(Exception ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("SERVER_ERROR", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = ex.getMessage();
    String code = "INVALID_ARGUMENT";

    if (message != null) {
      if (message.contains("not found")) {
        status = HttpStatus.NOT_FOUND;
        code = "NOT_FOUND";
      } else if (message.contains("already assigned")) {
        status = HttpStatus.CONFLICT;
        code = "ALREADY_ASSIGNED";
      }
    }

    List<ErrorDetail> errors = List.of(new ErrorDetail(code, message));
    return ResponseEntity.status(status).body(new ErrorResponse(errors));
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingImage(MissingServletRequestPartException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_IMAGE", "Image not provided"));
    return new ErrorResponse(errors);
  }
}
