package com.bharat.airport.domain.factory;
  
import com.bharat.airport.domain.model.Flight;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Factory for creating Flight aggregates Encapsulates complex creation logic and ensures proper
 * initialization
 */
@Component
public class FlightFactory {

  /** Create a new flight with validation */
  public Flight createFlight(
      String flightNumber,
      String origin,
      String destination,
      LocalDateTime scheduledDeparture,
      LocalDateTime scheduledArrival) {

    // Business validation
    if (scheduledDeparture.isAfter(scheduledArrival)) {
      throw new IllegalArgumentException("Departure time cannot be after arrival time");
    }

    if (scheduledDeparture.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Cannot schedule flight in the past");
    }

    return new Flight(flightNumber, origin, destination, scheduledDeparture, scheduledArrival);
  }

  /** Create flight with default timing (departure now + 2 hours, arrival + 4 hours) */
  public Flight createFlightWithDefaultTiming(
      String flightNumber, String origin, String destination) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime departure = now.plusHours(2);
    LocalDateTime arrival = now.plusHours(4);

    return createFlight(flightNumber, origin, destination, departure, arrival);
  }
}
