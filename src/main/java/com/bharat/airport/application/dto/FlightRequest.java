package com.bharat.airport.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {
  @NotBlank(message = "Flight number is required")
  private String flightNumber;

  @NotBlank(message = "Origin is required")
  private String origin;

  @NotBlank(message = "Destination is required")
  private String destination;

  @NotNull(message = "Scheduled departure is required")
  private LocalDateTime scheduledDeparture;

  @NotNull(message = "Scheduled arrival is required")
  private LocalDateTime scheduledArrival;
}
