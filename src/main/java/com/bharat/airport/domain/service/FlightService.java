package com.bharat.airport.domain.service;
  
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Domain Service for Flight operations Encapsulates business logic that involves multiple domain
 * objects
 */
@Service
@Transactional
public class FlightService {

  private final FlightRepository flightRepository;

  public FlightService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  /** Add passenger to flight with business validation */
  public void addPassengerToFlight(String flightNumber, Passenger passenger) {
    Flight flight =
        flightRepository
            .findByFlightNumber(flightNumber)
            .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));

    // Domain logic: validate seat assignment
    flight.addPassenger(passenger);
    flightRepository.save(flight);
  }

  /** Remove passenger from flight */
  public boolean removePassengerFromFlight(String flightNumber, String passengerId) {
    Flight flight =
        flightRepository
            .findByFlightNumber(flightNumber)
            .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));

    boolean removed = flight.removePassenger(passengerId);
    if (removed) {
      flightRepository.save(flight);
    }
    return removed;
  }

  /** Get flight with passengers */
  @Transactional(readOnly = true)
  public Flight getFlightWithPassengers(String flightNumber) {
    return flightRepository
        .findByFlightNumber(flightNumber)
        .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));
  }

  /** Find flights by route */
  @Transactional(readOnly = true)
  public List<Flight> findFlightsByRoute(String origin, String destination) {
    return flightRepository.findByRoute(origin, destination);
  }

  /** Find flights departing within time range */
  @Transactional(readOnly = true)
  public List<Flight> findFlightsByDepartureRange(LocalDateTime start, LocalDateTime end) {
    return flightRepository.findFlightsByDepartureTimeRange(start, end);
  }

  /** Check if flight exists */
  @Transactional(readOnly = true)
  public boolean flightExists(String flightNumber) {
    return flightRepository.findByFlightNumber(flightNumber).isPresent();
  }
}
