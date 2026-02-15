package com.bharat.airport.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bharat.airport.domain.Flight;
import com.bharat.airport.domain.Passenger;
import com.bharat.airport.domain.SeatAssignment;
import com.bharat.airport.domain.SeetClass;
import com.bharat.airport.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

  @Mock private FlightRepository flightRepository;

  @InjectMocks private FlightService flightService;

  private Flight flight;
  private Passenger passenger;

  @BeforeEach
  void setUp() {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    flight = new Flight("AB123", "JFK", "LAX", departure, arrival);

    SeatAssignment seat = new SeatAssignment("12A", SeetClass.Economy);
    passenger = new Passenger("John Doe", seat);
  }

  @Test
  void testAddPassengerToFlight() {
    when(flightRepository.findByFlightNumber("AB123")).thenReturn(Optional.of(flight));
    when(flightRepository.save(any(Flight.class))).thenReturn(flight);

    flightService.addPassengerToFlight("AB123", passenger);

    assertEquals(1, flight.getPassengerCount());
    verify(flightRepository).save(flight);
  }

  @Test
  void testAddPassengerToNonExistentFlight() {
    when(flightRepository.findByFlightNumber("INVALID")).thenReturn(Optional.empty());

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              flightService.addPassengerToFlight("INVALID", passenger);
            });

    assertTrue(exception.getMessage().contains("Flight not found"));
  }

  @Test
  void testRemovePassengerFromFlight() {
    passenger.setId(UUID.randomUUID());
    flight.addPassenger(passenger);

    when(flightRepository.findByFlightNumber("AB123")).thenReturn(Optional.of(flight));
    when(flightRepository.save(any(Flight.class))).thenReturn(flight);

    boolean removed = flightService.removePassengerFromFlight("AB123", "1");

    assertTrue(removed);
    assertEquals(0, flight.getPassengerCount());
    verify(flightRepository).save(flight);
  }

  @Test
  void testGetFlightWithPassengers() {
    when(flightRepository.findByFlightNumber("AB123")).thenReturn(Optional.of(flight));

    Flight result = flightService.getFlightWithPassengers("AB123");

    assertEquals(flight, result);
    assertEquals("AB123", result.getFlightNumber());
  }

  @Test
  void testFlightExists() {
    when(flightRepository.findByFlightNumber("AB123")).thenReturn(Optional.of(flight));
    when(flightRepository.findByFlightNumber("INVALID")).thenReturn(Optional.empty());

    assertTrue(flightService.flightExists("AB123"));
    assertFalse(flightService.flightExists("INVALID"));
  }
}
