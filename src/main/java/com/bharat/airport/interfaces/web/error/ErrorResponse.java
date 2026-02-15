package com.bharat.airport.interfaces.web.error;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse {
  private List<ErrorDetail> errors;

  public ErrorResponse(List<ErrorDetail> errors) {
    this.errors = errors;
  }
}

@Getter
@Setter
@ToString
class ErrorDetail {

  private String code;
  private String reason;
  private String datetime;

  public ErrorDetail(String code, String reason) {
    this.code = code;
    this.reason = reason;
    this.datetime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
  }
}
