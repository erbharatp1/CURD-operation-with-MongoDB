package com.bharat.airport.domain.exception;

public class AirportAlreadyExistsException extends RuntimeException {
  public AirportAlreadyExistsException(String code) {
    super("Airport already exists: " + code);
  }
}
