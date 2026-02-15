package com.bharat.airport.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlightTest {

  private Flight flight;
  private LocalDateTime departure;
  private LocalDateTime arrival;

  @BeforeEach
  void setUp() {
    departure = LocalDateTime.now().plusHours(2);
    arrival = LocalDateTime.now().plusHours(4);
    flight = new Flight("AB123", "JFK", "LAX", departure, arrival);
  }

  @Test
  void testFlightCreation() {
    assertEquals("AB123", flight.getFlightNumber());
    assertEquals("JFK", flight.getOrigin());
    assertEquals("LAX", flight.getDestination());
    assertEquals(departure, flight.getScheduledDeparture());
    assertEquals(arrival, flight.getScheduledArrival());
    assertTrue(flight.getPassengers().isEmpty());
  }

  @Test
  void testAddPassengerWithoutSeat() {
    Passenger passenger = new Passenger("John Doe", null);
    flight.addPassenger(passenger);

    assertEquals(1, flight.getPassengerCount());
    assertTrue(flight.getPassengers().contains(passenger));
  }

  @Test
  void testAddPassengerWithSeat() {
    SeatAssignment seat = new SeatAssignment("12A", SeetClass.Economy);
    Passenger passenger = new Passenger("John Doe", seat);

    flight.addPassenger(passenger);

    assertEquals(1, flight.getPassengerCount());
    assertEquals("12A", flight.getPassengers().get(0).getSeatAssignment().getSeatNumber());
  }

  @Test
  void testAddPassengerWithDuplicateSeat() {
    SeatAssignment seat = new SeatAssignment("12A", SeetClass.Economy);
    Passenger passenger1 = new Passenger("John Doe", seat);
    Passenger passenger2 = new Passenger("Jane Smith", seat);

    flight.addPassenger(passenger1);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              flight.addPassenger(passenger2);
            });

    assertTrue(exception.getMessage().contains("Seat 12A is already assigned"));
    assertEquals(1, flight.getPassengerCount());
  }

  //  @Test
  //  void testRemovePassenger() {
  //    Passenger passenger = new Passenger(UUID.randomUUID(), "John Doe", null);
  //    flight.addPassenger(passenger);
  //
  //    boolean removed = flight.removePassenger(passenger.getId().toString());
  //
  //    //assertTrue(removed);
  //    assertEquals(0, flight.getPassengerCount());
  //  }

  @Test
  void testRemoveNonExistentPassenger() {
    boolean removed = flight.removePassenger("999");
    assertFalse(removed);
  }
}
