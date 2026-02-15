package com.bharat.airport.interfaces.web.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidRequestException extends RuntimeException {
  private final String field;
  private final String errorMessage;
}
