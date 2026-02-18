package com.bharat.airport;

import static org.assertj.core.api.Assertions.assertThat;

import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.model.SeatAssignment;
import com.bharat.airport.domain.model.SeetClass;
import com.bharat.airport.domain.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("it")
class FlightIntegrationTest {

  private static final String DEMO_FLIGHT_NUMBER = "BA2490";

  @Container
  @ServiceConnection
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private FlightRepository flightRepository;

  @BeforeEach
  void setUp() {
    flightRepository.deleteAll();
    Flight demoFlight =
        new Flight(
            DEMO_FLIGHT_NUMBER,
            "LHR",
            "JFK",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(8));
    Passenger passenger =
        new Passenger(
            UUID.randomUUID(),
            "John Smith",
            new SeatAssignment("1A", SeetClass.First));
    demoFlight.setPassengers(Collections.singletonList(passenger));
    flightRepository.save(demoFlight);
  }

  @Test
  void shouldGetFlightByNumber_whenFlightExists() {
    ResponseEntity<Flight> response =
        restTemplate.getForEntity("/api/flights/" + DEMO_FLIGHT_NUMBER, Flight.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Flight flight = response.getBody();
    assertThat(flight).isNotNull();
    assertThat(flight.getFlightNumber()).isEqualTo(DEMO_FLIGHT_NUMBER);
    assertThat(flight.getOrigin()).isEqualTo("LHR");
    assertThat(flight.getDestination()).isEqualTo("JFK");
    assertThat(flight.getPassengers()).hasSize(1);
    assertThat(flight.getPassengers().get(0).getName()).isEqualTo("John Smith");
  }

  @Test
  void shouldReturnNotFound_whenFlightDoesNotExist() {
    ResponseEntity<Flight> response =
        restTemplate.getForEntity("/api/flights/XX999", Flight.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldCreateFlight_whenRequestIsValid() {
    LocalDateTime departure = LocalDateTime.now().plusDays(2).withNano(0);
    LocalDateTime arrival = departure.plusHours(5);
    FlightRequest flightRequest = new FlightRequest("UA87", "SFO", "SYD", departure, arrival);

    ResponseEntity<Flight> response =
        restTemplate.postForEntity("/api/flights", flightRequest, Flight.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Flight createdFlight = response.getBody();
    assertThat(createdFlight).isNotNull();
    assertThat(createdFlight.getFlightNumber()).isEqualTo("UA87");
    assertThat(createdFlight.getPassengers()).isEmpty();

    assertThat(flightRepository.findById("UA87")).isPresent();
  }

  @Test
  void shouldAddPassenger_whenFlightExists() {
    PassengerRequest passengerRequest =
        new PassengerRequest("Jane Doe", "22B", SeetClass.Economy);
    ResponseEntity<String> response =
        restTemplate.postForEntity(
            "/api/flights/" + DEMO_FLIGHT_NUMBER + "/passengers",
            passengerRequest,
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("Passenger added successfully");
    ResponseEntity<Flight> getFlightResponse =
        restTemplate.getForEntity("/api/flights/" + DEMO_FLIGHT_NUMBER, Flight.class);
    assertThat(getFlightResponse.getBody().getPassengers()).hasSize(2);
    assertThat(getFlightResponse.getBody().getPassengers())
        .anyMatch(p -> p.getName().equals("Jane Doe"));
  }

  @Test
  void shouldGetAllFlights_whenFlightsExist() {
    // Create a second flight to ensure we get a list
    Flight anotherFlight =
        new Flight(
            "AF32",
            "CDG",
            "IAD",
            LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(3).plusHours(9));
    flightRepository.save(anotherFlight);

    ResponseEntity<List<Flight>> response =
        restTemplate.exchange(
            "/api/flights",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Flight>>() {});

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody())
        .anyMatch(f -> f.getFlightNumber().equals(DEMO_FLIGHT_NUMBER));
    assertThat(response.getBody()).anyMatch(f -> f.getFlightNumber().equals("AF32"));
  }
}
