package com.bharat.airport.domain.factory;

import static org.junit.jupiter.api.Assertions.*;

import com.bharat.airport.domain.model.Flight;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlightFactoryTest {

  private FlightFactory flightFactory;

  @BeforeEach
  void setUp() {
    flightFactory = new FlightFactory();
  }

  @Test
  void shouldCreateFlightWhenDataIsValid() {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);

    Flight flight = flightFactory.createFlight("AB123", "JFK", "LAX", departure, arrival);

    assertNotNull(flight);
    assertEquals("AB123", flight.getFlightNumber());
    assertEquals("JFK", flight.getOrigin());
    assertEquals("LAX", flight.getDestination());
    assertEquals(departure, flight.getScheduledDeparture());
    assertEquals(arrival, flight.getScheduledArrival());
    assertTrue(flight.getPassengers().isEmpty());
  }

  @Test
  void shouldThrowExceptionWhenDepartureIsAfterArrival() {
    LocalDateTime departure = LocalDateTime.now().plusHours(4);
    LocalDateTime arrival = LocalDateTime.now().plusHours(2);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              flightFactory.createFlight("AB123", "JFK", "LAX", departure, arrival);
            });

    assertTrue(exception.getMessage().contains("Departure time cannot be after arrival time"));
  }

  @Test
  void shouldThrowExceptionWhenFlightScheduledInPast() {
    LocalDateTime departure = LocalDateTime.now().minusHours(1);
    LocalDateTime arrival = LocalDateTime.now().plusHours(1);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              flightFactory.createFlight("AB123", "JFK", "LAX", departure, arrival);
            });

    assertTrue(exception.getMessage().contains("Cannot schedule flight in the past"));
  }

  @Test
  void shouldCreateFlightWithDefaultTimingWhenRequested() {
    Flight flight = flightFactory.createFlightWithDefaultTiming("AB123", "JFK", "LAX");

    assertNotNull(flight);
    assertEquals("AB123", flight.getFlightNumber());
    assertEquals("JFK", flight.getOrigin());
    assertEquals("LAX", flight.getDestination());
    assertTrue(flight.getScheduledDeparture().isAfter(LocalDateTime.now()));
    assertTrue(flight.getScheduledArrival().isAfter(flight.getScheduledDeparture()));
  }
}
