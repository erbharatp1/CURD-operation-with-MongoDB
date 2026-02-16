package com.bharat.airport.interfaces.web.error;

import com.bharat.airport.config.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class InternalServerErrorException extends RuntimeException {
  public InternalServerErrorException(String message) {
    super(message);
  }
}
