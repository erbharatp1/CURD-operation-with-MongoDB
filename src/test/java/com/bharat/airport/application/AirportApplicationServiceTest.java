package com.bharat.airport.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bharat.airport.application.dto.AirportRequest;
import com.bharat.airport.domain.exception.AirportAlreadyExistsException;
import com.bharat.airport.domain.exception.AirportNotFoundException;
import com.bharat.airport.domain.model.Airport;
import com.bharat.airport.domain.repository.AirportRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirportApplicationServiceTest {

  @Mock private AirportRepository airportRepository;

  @InjectMocks private AirportApplicationService applicationService;

  private Airport airport;

  @BeforeEach
  void setUp() {
    airport =
        Airport.builder()
            .name("John F. Kennedy International Airport")
            .code("JFK")
            .isEnabled(true)
            .build();
  }

  @Test
  void shouldRegisterAirportWhenNotExists() {
    AirportRequest request =
        new AirportRequest("JFK", "John F. Kennedy International Airport", true);
    when(airportRepository.existsById("JFK")).thenReturn(false);
    when(airportRepository.save(any(Airport.class))).thenReturn(airport);

    Airport result = applicationService.registerAirport(request);

    assertNotNull(result);
    assertEquals("JFK", result.getCode());
    verify(airportRepository).save(any(Airport.class));
  }

  @Test
  void shouldThrowExceptionWhenRegisteringExistingAirport() {
    AirportRequest request =
        new AirportRequest("JFK", "John F. Kennedy International Airport", true);
    when(airportRepository.existsById("JFK")).thenReturn(true);

    assertThrows(
        AirportAlreadyExistsException.class, () -> applicationService.registerAirport(request));
  }

  @Test
  void shouldGetAirportWhenExists() {
    when(airportRepository.findById("JFK")).thenReturn(Optional.of(airport));

    Airport result = applicationService.getAirport("JFK");

    assertNotNull(result);
    assertEquals("JFK", result.getCode());
  }

  @Test
  void shouldThrowExceptionWhenGettingNonExistentAirport() {
    when(airportRepository.findById("INVALID")).thenReturn(Optional.empty());

    assertThrows(AirportNotFoundException.class, () -> applicationService.getAirport("INVALID"));
  }

  @Test
  void shouldGetAllAirports() {
    when(airportRepository.findAll()).thenReturn(Collections.singletonList(airport));

    List<Airport> result = applicationService.getAllAirports();

    assertEquals(1, result.size());
    assertEquals("JFK", result.get(0).getCode());
  }

  @Test
  void shouldDeleteAirportWhenExists() {
    when(airportRepository.existsById("JFK")).thenReturn(true);

    applicationService.deleteAirport("JFK");

    verify(airportRepository).deleteById("JFK");
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistentAirport() {
    when(airportRepository.existsById("INVALID")).thenReturn(false);

    assertThrows(AirportNotFoundException.class, () -> applicationService.deleteAirport("INVALID"));
  }
}
