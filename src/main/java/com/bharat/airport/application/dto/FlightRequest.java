package com.bharat.airport.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

  public FlightRequest() {}

  public FlightRequest(
      String flightNumber,
      String origin,
      String destination,
      LocalDateTime scheduledDeparture,
      LocalDateTime scheduledArrival) {
    this.flightNumber = flightNumber;
    this.origin = origin;
    this.destination = destination;
    this.scheduledDeparture = scheduledDeparture;
    this.scheduledArrival = scheduledArrival;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public LocalDateTime getScheduledDeparture() {
    return scheduledDeparture;
  }

  public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
    this.scheduledDeparture = scheduledDeparture;
  }

  public LocalDateTime getScheduledArrival() {
    return scheduledArrival;
  }

  public void setScheduledArrival(LocalDateTime scheduledArrival) {
    this.scheduledArrival = scheduledArrival;
  }
}
