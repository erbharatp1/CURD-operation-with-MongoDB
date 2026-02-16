package com.bharat.airport.application.dto;

import com.bharat.airport.config.ExcludeFromJacocoGeneratedReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ExcludeFromJacocoGeneratedReport
public class ApiResponse<T> {
  private T data;

  public ApiResponse(T data) {
    this.data = data;
  }
}
