package com.bharat.airport.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.factory.FlightFactory;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.model.SeetClass;
import com.bharat.airport.domain.repository.FlightRepository;
import com.bharat.airport.domain.service.FlightService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightApplicationServiceTest {

  @Mock private FlightService flightService;
  @Mock private FlightFactory flightFactory;
  @Mock private FlightRepository flightRepository;

  @InjectMocks private FlightApplicationService applicationService;

  private Flight flight;

  @BeforeEach
  void setUp() {
    flight = new Flight("AB123", "JFK", "LAX", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4));
  }

  @Test
  void shouldCreateFlightWhenRequested() {
    FlightRequest request = new FlightRequest("AB123", "JFK", "LAX", flight.getScheduledDeparture(), flight.getScheduledArrival());
    when(flightFactory.createFlight(any(), any(), any(), any(), any())).thenReturn(flight);
    when(flightRepository.save(any())).thenReturn(flight);

    Flight result = applicationService.createFlight(request);

    assertNotNull(result);
    assertEquals("AB123", result.getFlightNumber());
    verify(flightRepository).save(any());
  }

  @Test
  void shouldGetAllFlightsWhenRequested() {
    when(flightRepository.findAll()).thenReturn(Collections.singletonList(flight));

    List<Flight> result = applicationService.getAllFlights();

    assertEquals(1, result.size());
    assertEquals("AB123", result.get(0).getFlightNumber());
  }

  @Test
  void shouldGetFlightWhenFlightExists() {
    when(flightService.getFlightWithPassengers("AB123")).thenReturn(flight);

    Flight result = applicationService.getFlight("AB123");

    assertEquals("AB123", result.getFlightNumber());
  }

  @Test
  void shouldAddPassengerWhenRequested() {
    PassengerRequest request = new PassengerRequest("John Doe", "12A", SeetClass.Economy);

    applicationService.addPassenger("AB123", request);

    verify(flightService).addPassengerToFlight(eq("AB123"), any(Passenger.class));
  }

  @Test
  void shouldRemovePassengerWhenRequested() {
    when(flightService.removePassengerFromFlight("AB123", "pid")).thenReturn(true);

    boolean result = applicationService.removePassenger("AB123", "pid");

    assertTrue(result);
    verify(flightService).removePassengerFromFlight("AB123", "pid");
  }

  @Test
  void shouldDeleteFlightWhenFlightExists() {
    when(flightService.flightExists("AB123")).thenReturn(true);

    applicationService.deleteFlight("AB123");

    verify(flightRepository).deleteById("AB123");
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistentFlight() {
    when(flightService.flightExists("INVALID")).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> applicationService.deleteFlight("INVALID"));
  }
}
