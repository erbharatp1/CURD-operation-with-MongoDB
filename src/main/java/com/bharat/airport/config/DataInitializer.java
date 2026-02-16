package com.bharat.airport.config;

import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.model.SeatAssignment;
import com.bharat.airport.domain.model.SeetClass;
import com.bharat.airport.domain.repository.FlightRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Initializes sample data for demonstration purposes */
@Configuration
public class DataInitializer {

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

  @Bean
  @Profile("!test")
  public CommandLineRunner initData(FlightRepository flightRepository) {
    return args -> {
      if (flightRepository.count() == 0) {
        initializeSampleData(flightRepository);
        log.info("Sample data initialized successfully");
      }
    };
  }

  private void initializeSampleData(FlightRepository flightRepository) {
    // Create sample flights
    LocalDateTime now = LocalDateTime.now();

    // Flight 1: JFK to LAX
    Flight flight1 = new Flight("UA101", "JFK", "LAX", now.plusHours(2), now.plusHours(6));
    flight1.addPassenger(new Passenger("John Doe", new SeatAssignment("12A", SeetClass.Economy)));
    flight1.addPassenger(new Passenger("Jane Smith", new SeatAssignment("12B", SeetClass.Economy)));

    // Flight 2: LAX to JFK
    Flight flight2 = new Flight("UA102", "LAX", "JFK", now.plusHours(4), now.plusHours(8));
    flight2.addPassenger(
        new Passenger("Bob Johnson", new SeatAssignment("1A", SeetClass.Business)));

    // Flight 3: ORD to MIA
    Flight flight3 = new Flight("AA203", "ORD", "MIA", now.plusHours(3), now.plusHours(6));
    flight3.addPassenger(
        new Passenger("Alice Brown", new SeatAssignment("15C", SeetClass.Economy)));
    flight3.addPassenger(
        new Passenger("Charlie Wilson", new SeatAssignment("15D", SeetClass.Economy)));
    flight3.addPassenger(
        new Passenger("Diana Davis", new SeatAssignment("2A", SeetClass.Business)));

    // Save flights
    flightRepository.save(flight1);
    flightRepository.save(flight2);
    flightRepository.save(flight3);

    log.info("Created {} sample flights", flightRepository.count());
  }
}
