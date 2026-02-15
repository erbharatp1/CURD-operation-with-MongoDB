package com.bharat.airport.interfaces.web.error;

public class InternalServerErrorException extends RuntimeException {
  public InternalServerErrorException(String message) {
    super(message);
  }
}
