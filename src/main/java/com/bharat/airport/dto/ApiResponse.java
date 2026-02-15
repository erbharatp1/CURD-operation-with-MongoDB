package com.bharat.airport.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
  private T data;

  public ApiResponse(T data) {
    this.data = data;
  }
}
