package com.bharat.airport.interfaces.web.error;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
  private final String errorMessage;

  public ResourceNotFoundException(String message) {
    super(message);
    this.errorMessage = message;
  }
}
