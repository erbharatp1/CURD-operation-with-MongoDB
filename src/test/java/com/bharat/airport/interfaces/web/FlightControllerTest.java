package com.bharat.airport.interfaces.web;

import static org.junit.jupiter.api.Assertions.*;

import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.SeetClass;
import com.bharat.airport.domain.repository.FlightRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FlightControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private FlightRepository flightRepository;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/flights";
    flightRepository.deleteAll();
  }

  @Test
  void shouldCreateFlightWhenValidRequest() {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);

    FlightRequest flightRequest = new FlightRequest("TEST123", "JFK", "LAX", departure, arrival);

    ResponseEntity<Flight> response =
        restTemplate.postForEntity(baseUrl, flightRequest, Flight.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("TEST123", response.getBody().getFlightNumber());
    assertEquals("JFK", response.getBody().getOrigin());
    assertEquals("LAX", response.getBody().getDestination());
  }

  @Test
  void shouldGetAllFlightsWhenRequested() {
    // Create a flight first
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    ResponseEntity<Flight[]> response = restTemplate.getForEntity(baseUrl, Flight[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().length);
    assertEquals("TEST123", response.getBody()[0].getFlightNumber());
  }

  @Test
  void shouldGetFlightByNumberWhenFlightExists() {
    // Create a flight first
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    ResponseEntity<Flight> response = restTemplate.getForEntity(baseUrl + "/TEST123", Flight.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("TEST123", response.getBody().getFlightNumber());
  }

  @Test
  void shouldReturnNotFoundWhenFlightDoesNotExist() {
    ResponseEntity<Flight> response = restTemplate.getForEntity(baseUrl + "/INVALID", Flight.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldAddPassengerToFlightWhenSeatAvailable() {
    // Create a flight first
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    PassengerRequest passengerRequest = new PassengerRequest("John Doe", "12A", SeetClass.Economy);

    ResponseEntity<String> response =
        restTemplate.postForEntity(baseUrl + "/TEST123/passengers", passengerRequest, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Passenger added successfully", response.getBody());
  }

  @Test
  void shouldReturnConflictWhenAddingPassengerToOccupiedSeat() {
    // Create a flight first
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    PassengerRequest passengerRequest1 = new PassengerRequest("John Doe", "12A", SeetClass.Economy);
    PassengerRequest passengerRequest2 =
        new PassengerRequest("Jane Smith", "12A", SeetClass.Economy);

    // Add first passenger
    restTemplate.postForEntity(baseUrl + "/TEST123/passengers", passengerRequest1, String.class);

    // Try to add second passenger with same seat
    ResponseEntity<String> response =
        restTemplate.postForEntity(
            baseUrl + "/TEST123/passengers", passengerRequest2, String.class);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertTrue(response.getBody().contains("Seat 12A is already assigned"));
  }

  @Test
  void shouldDeleteFlightWhenFlightExists() {
    // Create a flight first
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);
    Flight flight = new Flight("TEST123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    restTemplate.delete(baseUrl + "/TEST123");

    // Verify flight is deleted
    ResponseEntity<Flight> response = restTemplate.getForEntity(baseUrl + "/TEST123", Flight.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
