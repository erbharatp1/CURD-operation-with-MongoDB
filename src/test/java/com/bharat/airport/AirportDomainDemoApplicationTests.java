package com.bharat.airport;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.bharat.airport.domain.Flight;
import com.bharat.airport.repository.FlightRepository;
import com.bharat.airport.repository.PassengerRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AirportDomainDemoApplicationTests {

  @Autowired private FlightRepository flightRepository;

  @Autowired private PassengerRepository passengerRepository;

  @Test
  void contextLoads() {
    // Verify that Spring context loads successfully
    assertNotNull(flightRepository);
    assertNotNull(passengerRepository);
  }

  @Test
  void testCreateFlight() {
    LocalDateTime departure = LocalDateTime.now().plusHours(2);
    LocalDateTime arrival = LocalDateTime.now().plusHours(4);

    Flight flight = new Flight("AB123", "JFK", "LAX", departure, arrival);
    flightRepository.save(flight);

    Flight savedFlight = flightRepository.findByFlightNumber("AB123").orElse(null);
    assertNotNull(savedFlight);
  }
}
