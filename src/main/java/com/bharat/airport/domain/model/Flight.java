package com.bharat.airport.domain.model;

import com.bharat.airport.domain.exception.SeatAlreadyAssignedException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

  public Flight(
      String flightNumber,
      String origin,
      String destination,
      LocalDateTime scheduledDeparture,
      LocalDateTime scheduledArrival) {
    validateTimes(scheduledDeparture, scheduledArrival);
    this.flightNumber = flightNumber;
    this.origin = origin;
    this.destination = destination;
    this.scheduledDeparture = scheduledDeparture;
    this.scheduledArrival = scheduledArrival;
    this.passengers = new ArrayList<>();
  }

  private void validateTimes(LocalDateTime departure, LocalDateTime arrival) {
    if (departure != null && arrival != null) {
      if (departure.isAfter(arrival)) {
        throw new IllegalArgumentException("Departure time cannot be after arrival time");
      }
      if (departure.isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("Cannot schedule flight in the past");
      }
    }
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
        throw new SeatAlreadyAssignedException(passenger.getSeatAssignment().getSeatNumber());
      }
    }
    this.passengers.add(passenger);
  }

  public boolean removePassenger(String passengerId) {
    if (passengerId == null) return false;
    return passengers.removeIf(p -> p.getId() != null && p.getId().toString().equals(passengerId));
  }
}
