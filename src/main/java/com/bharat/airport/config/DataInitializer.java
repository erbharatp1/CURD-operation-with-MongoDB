package com.bharat.airport.config;

import com.bharat.airport.domain.Flight;
import com.bharat.airport.domain.Passenger;
import com.bharat.airport.domain.SeatAssignment;
import com.bharat.airport.domain.SeetClass;
import com.bharat.airport.repository.FlightRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Initializes sample data for demonstration purposes */
@Component
public class DataInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
  private final FlightRepository flightRepository;

  public DataInitializer(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    // Only initialize data if not in test environment
    String[] activeProfiles = System.getProperty("spring.profiles.active", "").split(",");
    boolean isTestProfile = java.util.Arrays.asList(activeProfiles).contains("test");

    if (!isTestProfile && flightRepository.count() == 0) {
      initializeSampleData();
      log.info("Sample data initialized successfully");
    }
  }

  private void initializeSampleData() {
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
