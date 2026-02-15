package com.bharat.airport.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
  private final String errorMessage;

  public BadRequestException(String message) {
    super(message);
    this.errorMessage = message;
  }
}
