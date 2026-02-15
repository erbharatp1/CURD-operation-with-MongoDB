package com.bharat.airport.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("java:S6204")
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<ErrorDetail> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorDetail("VALIDATION_ERROR", error.getDefaultMessage()))
            .collect(Collectors.toList());
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(HandlerMethodValidationException ex) {
    List<ErrorDetail> errors =
        ex.getAllErrors().stream()
            .map(error -> new ErrorDetail("VALIDATION_ERROR", error.getDefaultMessage()))
            .collect(Collectors.toList());
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingRequestBody(HttpMessageNotReadableException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("MALFORMED_REQUEST_BODY", ex.getMessage()));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(InvalidRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidRequestException(InvalidRequestException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_DATA", ex.getErrorMessage()));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidRequestException(BadRequestException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_DATA", ex.getMessage()));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGenericException(Exception ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("SERVER_ERROR", ex.getMessage()));
    log.atError()
        .setMessage(ex.getMessage())
        .addKeyValue(ex.getMessage(), errors)
        .setCause(ex)
        .log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleStoreNotFoundException(Exception ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("NOT_FOUND", ex.getMessage()));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleStoreAlreadyExistsException(Exception ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("STORE_ALREADY_EXISTS", ex.getMessage()));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }


  @ExceptionHandler(MissingServletRequestPartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingImage(MissingServletRequestPartException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_IMAGE", "Image not provided"));
    log.atError().setMessage(ex.getMessage()).addKeyValue(ex.getMessage(), errors).log();
    return new ErrorResponse(errors);
  }
}
