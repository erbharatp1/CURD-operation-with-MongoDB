package com.bharat.airport.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequest {
  @NotBlank(message = "Airport code is required")
  private String code;

  @NotBlank(message = "Airport name is required")
  private String name;

  private boolean isEnabled = true;
}
