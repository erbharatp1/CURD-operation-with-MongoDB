package com.bharat.airport.controller;

import com.bharat.airport.domain.Flight;
import com.bharat.airport.domain.Passenger;
import com.bharat.airport.domain.SeatAssignment;
import com.bharat.airport.dto.FlightRequest;
import com.bharat.airport.dto.PassengerRequest;
import com.bharat.airport.factory.FlightFactory;
import com.bharat.airport.repository.FlightRepository;
import com.bharat.airport.service.FlightService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

  private final FlightService flightService;
  private final FlightFactory flightFactory;
  private final FlightRepository flightRepository;

  public FlightController(
      FlightService flightService, FlightFactory flightFactory, FlightRepository flightRepository) {
    this.flightService = flightService;
    this.flightFactory = flightFactory;
    this.flightRepository = flightRepository;
  }

  /** Create a new flight */
  @PostMapping
  public ResponseEntity<?> createFlight(@Valid @RequestBody FlightRequest flightRequest) {
    try {
      Flight flight =
          flightFactory.createFlight(
              flightRequest.getFlightNumber(),
              flightRequest.getOrigin(),
              flightRequest.getDestination(),
              flightRequest.getScheduledDeparture(),
              flightRequest.getScheduledArrival());

      Flight savedFlight = flightRepository.save(flight);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /** Get all flights */
  @GetMapping
  public ResponseEntity<List<Flight>> getAllFlights() {
    List<Flight> flights = flightRepository.findAll();
    return ResponseEntity.ok(flights);
  }

  /** Get flight by flight number */
  @GetMapping("/{flightNumber}")
  public ResponseEntity<Flight> getFlight(@PathVariable String flightNumber) {
    try {
      Flight flight = flightService.getFlightWithPassengers(flightNumber);
      return ResponseEntity.ok(flight);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /** Add passenger to flight */
  @PostMapping("/{flightNumber}/passengers")
  public ResponseEntity<String> addPassenger(
      @PathVariable String flightNumber, @Valid @RequestBody PassengerRequest passengerRequest) {
    try {
      SeatAssignment seatAssignment = null;
      if (passengerRequest.getSeatNumber() != null && passengerRequest.getSeatClass() != null) {
        seatAssignment =
            new SeatAssignment(passengerRequest.getSeatNumber(), passengerRequest.getSeatClass());
      }

      Passenger passenger = new Passenger(UUID.randomUUID(),passengerRequest.getName(), seatAssignment);
      flightService.addPassengerToFlight(flightNumber, passenger);

      return ResponseEntity.ok("Passenger added successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  /** Remove passenger from flight */
  @DeleteMapping("/{flightNumber}/passengers/{passengerId}")
  public ResponseEntity<String> removePassenger(
      @PathVariable String flightNumber, @PathVariable String passengerId) {
    try {
      boolean removed = flightService.removePassengerFromFlight(flightNumber, passengerId);
      if (removed) {
        return ResponseEntity.ok("Passenger removed successfully");
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /** Get flights by route */
  @GetMapping("/route")
  public ResponseEntity<List<Flight>> getFlightsByRoute(
      @RequestParam String origin, @RequestParam String destination) {
    List<Flight> flights = flightService.findFlightsByRoute(origin, destination);
    return ResponseEntity.ok(flights);
  }

  /** Get flights by departure time range */
  @GetMapping("/departures")
  public ResponseEntity<List<Flight>> getFlightsByDepartureRange(
      @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
    List<Flight> flights = flightService.findFlightsByDepartureRange(start, end);
    return ResponseEntity.ok(flights);
  }

  /** Delete flight */
  @DeleteMapping("/{flightNumber}")
  public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
    if (flightService.flightExists(flightNumber)) {
      flightRepository.deleteById(flightNumber);
      return ResponseEntity.ok("Flight deleted successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
