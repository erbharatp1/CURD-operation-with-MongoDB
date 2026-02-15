package com.bharat.airport.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "flights")
public class Flight {
  @Id
  @NotBlank(message = "Flight number is required")
  @Indexed(unique = true)
  private String flightNumber;

  @NotBlank(message = "Origin is required")
  private String origin;

  @NotBlank(message = "Destination is required")
  private String destination;

  @NotNull(message = "Scheduled departure is required")
  private LocalDateTime scheduledDeparture;

  @NotNull(message = "Scheduled arrival is required")
  private LocalDateTime scheduledArrival;

  private List<Passenger> passengers = new ArrayList<>();

  public Flight() {}

  public Flight(
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
    this.passengers = new ArrayList<>();
  }

  /** Business method to add passenger with seat validation */
  public void addPassenger(Passenger passenger) {
    if (passenger.getSeatAssignment() != null) {
      boolean seatTaken =
          passengers.stream()
              .anyMatch(
                  p ->
                      p.getSeatAssignment() != null
                          && p.getSeatAssignment().equals(passenger.getSeatAssignment()));

      if (seatTaken) {
        throw new IllegalArgumentException(
            "Seat " + passenger.getSeatAssignment().getSeatNumber() + " is already assigned");
      }
    }
    this.passengers.add(passenger);
  }

  /** Business method to remove passenger */
  public boolean removePassenger(String passengerId) {
    return passengers.removeIf(p -> p.getId().equals(passengerId));
  }

  /** Get passenger count */
  public int getPassengerCount() {
    return passengers.size();
  }

  // Getters and Setters
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

  public List<Passenger> getPassengers() {
    return passengers;
  }

  public void setPassengers(List<Passenger> passengers) {
    this.passengers = passengers;
  }
}
