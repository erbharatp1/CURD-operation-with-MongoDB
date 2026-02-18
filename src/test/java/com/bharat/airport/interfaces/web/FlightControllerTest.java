package com.bharat.airport.interfaces.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bharat.airport.application.FlightApplicationService;
import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.exception.FlightNotFoundException;
import com.bharat.airport.domain.exception.SeatAlreadyAssignedException;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.SeetClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private FlightApplicationService flightApplicationService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldCreateFlightWhenValidRequest() throws Exception {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    FlightRequest flightRequest = new FlightRequest("TEST123", "JFK", "LAX", departure, arrival);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);

    when(flightApplicationService.createFlight(any(FlightRequest.class))).thenReturn(flight);

    mockMvc
        .perform(
            post("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.flightNumber").value("TEST123"))
        .andExpect(jsonPath("$.origin").value("JFK"))
        .andExpect(jsonPath("$.destination").value("LAX"));
  }

  @Test
  void shouldGetAllFlightsWhenRequested() throws Exception {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);

    when(flightApplicationService.getAllFlights()).thenReturn(List.of(flight));

    mockMvc
        .perform(get("/api/flights"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].flightNumber").value("TEST123"));
  }

  @Test
  void shouldGetFlightByNumberWhenFlightExists() throws Exception {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);

    when(flightApplicationService.getFlight("TEST123")).thenReturn(flight);

    mockMvc
        .perform(get("/api/flights/TEST123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.flightNumber").value("TEST123"));
  }

  @Test
  void shouldReturnNotFoundWhenFlightDoesNotExist() throws Exception {
    when(flightApplicationService.getFlight("INVALID"))
        .thenThrow(new FlightNotFoundException("INVALID"));

    mockMvc.perform(get("/api/flights/INVALID")).andExpect(status().isNotFound());
  }

  @Test
  void shouldAddPassengerToFlightWhenSeatAvailable() throws Exception {
    PassengerRequest passengerRequest = new PassengerRequest("John Doe", "12A", SeetClass.Economy);

    doNothing()
        .when(flightApplicationService)
        .addPassenger(eq("TEST123"), any(PassengerRequest.class));

    mockMvc
        .perform(
            post("/api/flights/TEST123/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("Passenger added successfully"));
  }

  @Test
  void shouldReturnConflictWhenAddingPassengerToOccupiedSeat() throws Exception {
    PassengerRequest passengerRequest = new PassengerRequest("John Doe", "12A", SeetClass.Economy);

    doThrow(new SeatAlreadyAssignedException("12A"))
        .when(flightApplicationService)
        .addPassenger(eq("TEST123"), any(PassengerRequest.class));

    mockMvc
        .perform(
            post("/api/flights/TEST123/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerRequest)))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldDeleteFlightWhenFlightExists() throws Exception {
    doNothing().when(flightApplicationService).deleteFlight("TEST123");

    mockMvc
        .perform(delete("/api/flights/TEST123"))
        .andExpect(status().isOk())
        .andExpect(content().string("Flight deleted successfully"));
  }

  @Test
  void shouldReturnNotFoundWhenDeletingNonExistentFlight() throws Exception {
    doThrow(new FlightNotFoundException("INVALID"))
        .when(flightApplicationService)
        .deleteFlight("INVALID");

    mockMvc.perform(delete("/api/flights/INVALID")).andExpect(status().isNotFound());
  }

  @Test
  void shouldGetFlightsByRoute() throws Exception {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);

    when(flightApplicationService.findFlightsByRoute("JFK", "LAX")).thenReturn(List.of(flight));

    mockMvc
        .perform(get("/api/flights/route").param("origin", "JFK").param("destination", "LAX"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].origin").value("JFK"))
        .andExpect(jsonPath("$[0].destination").value("LAX"));
  }

  @Test
  void shouldGetFlightsByDepartureRange() throws Exception {
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusDays(1);
    Flight flight = new Flight("TEST123", "JFK", "LAX", start.plusHours(2), start.plusHours(4));

    when(flightApplicationService.findFlightsByDepartureRange(
            any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(List.of(flight));

    mockMvc
        .perform(
            get("/api/flights/departures")
                .param("start", start.toString())
                .param("end", end.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void shouldRemovePassengerWhenPassengerExists() throws Exception {
    when(flightApplicationService.removePassenger("TEST123", "passenger-id")).thenReturn(true);

    mockMvc
        .perform(delete("/api/flights/TEST123/passengers/passenger-id"))
        .andExpect(status().isOk())
        .andExpect(content().string("Passenger removed successfully"));
  }

  @Test
  void shouldReturnNotFoundWhenRemovingNonExistentPassenger() throws Exception {
    when(flightApplicationService.removePassenger("TEST123", "invalid-id")).thenReturn(false);

    mockMvc
        .perform(delete("/api/flights/TEST123/passengers/invalid-id"))
        .andExpect(status().isNotFound());
  }
}
