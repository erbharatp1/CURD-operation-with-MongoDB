package com.bharat.airport.interfaces.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bharat.airport.application.AirportApplicationService;
import com.bharat.airport.application.dto.AirportRequest;
import com.bharat.airport.domain.exception.AirportNotFoundException;
import com.bharat.airport.domain.model.Airport;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AirportApplicationService airportApplicationService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldRegisterAirportWhenValidRequest() throws Exception {
    AirportRequest request = new AirportRequest("JFK", "John F. Kennedy", true);
    Airport airport = Airport.builder().name("John F. Kennedy").code("JFK").isEnabled(true).build();

    when(airportApplicationService.registerAirport(any(AirportRequest.class))).thenReturn(airport);

    mockMvc
        .perform(
            post("/api/airports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value("JFK"))
        .andExpect(jsonPath("$.name").value("John F. Kennedy"))
        .andExpect(jsonPath("$.enabled").value(true));
  }

  @Test
  void shouldGetAirportWhenExists() throws Exception {
    Airport airport = Airport.builder().name("John F. Kennedy").code("JFK").isEnabled(true).build();
    when(airportApplicationService.getAirport("JFK")).thenReturn(airport);

    mockMvc
        .perform(get("/api/airports/JFK"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("JFK"));
  }

  @Test
  void shouldReturnNotFoundWhenAirportDoesNotExist() throws Exception {
    when(airportApplicationService.getAirport("INVALID"))
        .thenThrow(new AirportNotFoundException("INVALID"));

    mockMvc.perform(get("/api/airports/INVALID")).andExpect(status().isNotFound());
  }

  @Test
  void shouldGetAllAirports() throws Exception {
    Airport airport = Airport.builder().name("John F. Kennedy").code("JFK").isEnabled(true).build();
    when(airportApplicationService.getAllAirports()).thenReturn(List.of(airport));

    mockMvc
        .perform(get("/api/airports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].code").value("JFK"));
  }

  @Test
  void shouldDeleteAirportWhenExists() throws Exception {
    doNothing().when(airportApplicationService).deleteAirport("JFK");

    mockMvc
        .perform(delete("/api/airports/JFK"))
        .andExpect(status().isOk())
        .andExpect(content().string("Airport deleted successfully"));
  }
}
