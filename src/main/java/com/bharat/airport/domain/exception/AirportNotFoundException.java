package com.bharat.airport.domain.exception;

public class AirportNotFoundException extends RuntimeException {
  public AirportNotFoundException(String code) {
    super("Airport not found: " + code);
  }
}
