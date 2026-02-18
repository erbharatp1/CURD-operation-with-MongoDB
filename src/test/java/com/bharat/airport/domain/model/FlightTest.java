package com.bharat.airport.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.bharat.airport.domain.exception.SeatAlreadyAssignedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
  void shouldReturnFlightDataWhenCreated() {
    assertEquals("AB123", flight.getFlightNumber());
    assertEquals("JFK", flight.getOrigin());
    assertEquals("LAX", flight.getDestination());
    assertEquals(departure, flight.getScheduledDeparture());
    assertEquals(arrival, flight.getScheduledArrival());
    assertTrue(flight.getPassengers().isEmpty());
  }

  @Test
  void shouldAddPassengerWhenNoSeatAssigned() {
    Passenger passenger = new Passenger("John Doe", null);
    flight.addPassenger(passenger);

    assertEquals(1, flight.getPassengers().size());
    assertTrue(flight.getPassengers().contains(passenger));
  }

  @Test
  void shouldAddPassengerWhenValidSeatAssigned() {
    SeatAssignment seat = new SeatAssignment("12A", SeetClass.Economy);
    Passenger passenger = new Passenger("John Doe", seat);

    flight.addPassenger(passenger);

    assertEquals(1, flight.getPassengers().size());
    assertEquals("12A", flight.getPassengers().get(0).getSeatAssignment().getSeatNumber());
  }

  @Test
  void shouldThrowExceptionWhenAddingPassengerWithDuplicateSeat() {
    SeatAssignment seat = new SeatAssignment("12A", SeetClass.Economy);
    Passenger passenger1 = new Passenger("John Doe", seat);
    Passenger passenger2 = new Passenger("Jane Smith", seat);

    flight.addPassenger(passenger1);

    SeatAlreadyAssignedException exception =
        assertThrows(
            SeatAlreadyAssignedException.class,
            () -> {
              flight.addPassenger(passenger2);
            });

    assertTrue(exception.getMessage().contains("Seat 12A is already assigned"));
    assertEquals(1, flight.getPassengers().size());
  }

  @Test
  void shouldRemovePassengerWhenPassengerExists() {
    UUID id = UUID.randomUUID();
    Passenger passenger = new Passenger(id, "John Doe", null);
    flight.addPassenger(passenger);

    boolean removed = flight.removePassenger(id.toString());

    assertTrue(removed);
    assertEquals(0, flight.getPassengers().size());
  }

  @Test
  void shouldReturnFalseWhenRemovingNonExistentPassenger() {
    boolean removed = flight.removePassenger(UUID.randomUUID().toString());
    assertFalse(removed);
  }

  @Test
  void shouldThrowExceptionWhenDepartureTimeIsAfterArrivalTime() {
    LocalDateTime dep = LocalDateTime.now().plusHours(5);
    LocalDateTime arr = LocalDateTime.now().plusHours(3);
    assertThrows(IllegalArgumentException.class, () -> new Flight("F1", "O", "D", dep, arr));
  }

  @Test
  void shouldThrowExceptionWhenDepartureTimeIsInPast() {
    LocalDateTime dep = LocalDateTime.now().minusHours(1);
    LocalDateTime arr = LocalDateTime.now().plusHours(1);
    assertThrows(IllegalArgumentException.class, () -> new Flight("F1", "O", "D", dep, arr));
  }

  @Test
  void shouldNotThrowExceptionWhenTimesAreNull() {
    assertDoesNotThrow(() -> new Flight("F1", "O", "D", null, LocalDateTime.now()));
    assertDoesNotThrow(() -> new Flight("F1", "O", "D", LocalDateTime.now(), null));
  }

  @Test
  void shouldHandleNullPassengerIdWhenRemovingPassenger() {
    assertFalse(flight.removePassenger(null));
  }

  @Test
  void shouldSetAndGetFlightProperties() {
    Flight f = new Flight();
    f.setFlightNumber("FN123");
    f.setOrigin("ORG");
    f.setDestination("DST");
    LocalDateTime dep = LocalDateTime.now().plusHours(1);
    LocalDateTime arr = LocalDateTime.now().plusHours(2);
    f.setScheduledDeparture(dep);
    f.setScheduledArrival(arr);

    assertEquals("FN123", f.getFlightNumber());
    assertEquals("ORG", f.getOrigin());
    assertEquals("DST", f.getDestination());
    assertEquals(dep, f.getScheduledDeparture());
    assertEquals(arr, f.getScheduledArrival());
  }

  @Test
  void shouldSetAndGetPassengers() {
    Flight f = new Flight();
    List<Passenger> passengers = new ArrayList<>();
    passengers.add(new Passenger("P1", null));
    f.setPassengers(passengers);
    assertEquals(1, f.getPassengers().size());
    assertEquals("P1", f.getPassengers().get(0).getName());
  }
}
