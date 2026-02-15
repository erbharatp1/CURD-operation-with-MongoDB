package com.bharat.airport.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
  private final String errorMessage;

  public ResourceAlreadyExistsException(String message) {
    super(message);
    this.errorMessage = message;
  }
}
