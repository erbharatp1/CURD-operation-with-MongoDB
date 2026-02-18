package com.bharat.airport.interfaces.web.error;

import com.bharat.airport.config.ExcludeFromJacocoGeneratedReport;
import com.bharat.airport.domain.exception.AirportAlreadyExistsException;
import com.bharat.airport.domain.exception.AirportNotFoundException;
import com.bharat.airport.domain.exception.FlightNotFoundException;
import com.bharat.airport.domain.exception.SeatAlreadyAssignedException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
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

  @ExceptionHandler(FlightNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleFlightNotFoundException(FlightNotFoundException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("FLIGHT_NOT_FOUND", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(AirportNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleAirportNotFoundException(AirportNotFoundException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("AIRPORT_NOT_FOUND", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(AirportAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleAirportAlreadyExistsException(AirportAlreadyExistsException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("AIRPORT_ALREADY_EXISTS", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(SeatAlreadyAssignedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleSeatAlreadyAssignedException(SeatAlreadyAssignedException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("SEAT_ALREADY_ASSIGNED", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_ARGUMENT", ex.getMessage()));
    return new ErrorResponse(errors);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingImage(MissingServletRequestPartException ex) {
    List<ErrorDetail> errors = List.of(new ErrorDetail("INVALID_IMAGE", "Image not provided"));
    return new ErrorResponse(errors);
  }
}
