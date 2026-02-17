package com.bharat.airport;

import static org.assertj.core.api.Assertions.assertThat;

import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.SeetClass;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

  @Container
  @ServiceConnection
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void shouldCreateFlightWhenValidRequest() {
    // 1. Create a flight
    LocalDateTime departure = LocalDateTime.now().plusDays(1).withNano(0);
    LocalDateTime arrival = departure.plusHours(3);
    FlightRequest flightRequest = new FlightRequest("FL123", "NYC", "LAX", departure, arrival);

    ResponseEntity<Flight> createResponse =
        restTemplate.postForEntity("/api/flights", flightRequest, Flight.class);

    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Flight createdFlight = createResponse.getBody();
    assertThat(createdFlight).isNotNull();
    assertThat(createdFlight.getFlightNumber()).isEqualTo("FL123");
  }

  @Test
  void shouldAddPassengerWhenFlightExists() {
    // 2. Add a passenger
    PassengerRequest passengerRequest = new PassengerRequest("John Doe", "12A", SeetClass.Economy);
    ResponseEntity<String> addPassengerResponse =
        restTemplate.postForEntity(
            "/api/flights/FL123/passengers", passengerRequest, String.class);

    assertThat(addPassengerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(addPassengerResponse.getBody()).isEqualTo("Passenger added successfully");
  }

  @Test
  void shouldGetFlightWhenFlightExists() {
    // 3. Get the flight and verify passenger
    ResponseEntity<Flight> getFlightResponse =
        restTemplate.getForEntity("/api/flights/FL123", Flight.class);

    assertThat(getFlightResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Flight flightWithPassenger = getFlightResponse.getBody();
    assertThat(flightWithPassenger).isNotNull();
    assertThat(flightWithPassenger.getPassengers()).hasSize(1);
    assertThat(flightWithPassenger.getPassengers().get(0).getName()).isEqualTo("John Doe");
  }

  @Test
  void shouldGetAllFlightsWhenFlightsExist() {
    // 4. Get all flights
    ResponseEntity<List<Flight>> getAllResponse =
        restTemplate.exchange(
            "/api/flights",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Flight>>() {});

    assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getAllResponse.getBody()).anyMatch(f -> f.getFlightNumber().equals("FL123"));
  }
}
