package com.bharat.airport.domain.service;

import com.bharat.airport.domain.exception.FlightNotFoundException;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Domain Service for Flight operations Encapsulates business logic that involves multiple domain
 * objects
 */
@Service
@Transactional
@Slf4j
public class FlightService {

  private final FlightRepository flightRepository;

  public FlightService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  public void addPassengerToFlight(String flightNumber, Passenger passenger) {
    log.info("Adding Passenger to Flight {}", flightNumber);
    Flight flight =
        flightRepository
            .findByFlightNumber(flightNumber)
            .orElseThrow(() -> new FlightNotFoundException(flightNumber));

    flight.addPassenger(passenger);
    flightRepository.save(flight);
    log.info("Added Passenger to Flight {}", flightNumber);
  }

  /** Remove passenger from flight */
  public boolean removePassengerFromFlight(String flightNumber, String passengerId) {
    Flight flight =
        flightRepository
            .findByFlightNumber(flightNumber)
            .orElseThrow(() -> new FlightNotFoundException(flightNumber));

    boolean removed = flight.removePassenger(passengerId);
    if (removed) {
      flightRepository.save(flight);
    }
    return removed;
  }

  @Transactional(readOnly = true)
  public Flight getFlightWithPassengers(String flightNumber) {
    return flightRepository
        .findByFlightNumber(flightNumber)
        .orElseThrow(() -> new FlightNotFoundException(flightNumber));
  }

  @Transactional(readOnly = true)
  public List<Flight> findFlightsByRoute(String origin, String destination) {
    return flightRepository.findByRoute(origin, destination);
  }

  @Transactional(readOnly = true)
  public List<Flight> findFlightsByDepartureRange(LocalDateTime start, LocalDateTime end) {
    return flightRepository.findFlightsByDepartureTimeRange(start, end);
  }

  @Transactional(readOnly = true)
  public boolean flightExists(String flightNumber) {
    return flightRepository.findByFlightNumber(flightNumber).isPresent();
  }
}
