package com.bharat.airport.interfaces.web;

import com.bharat.airport.application.FlightApplicationService;
import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

  private final FlightApplicationService flightApplicationService;

  public FlightController(FlightApplicationService flightApplicationService) {
    this.flightApplicationService = flightApplicationService;
  }

  /** Create a new flight */
  @PostMapping
  public ResponseEntity<?> createFlight(@Valid @RequestBody FlightRequest flightRequest) {
    try {
      Flight savedFlight = flightApplicationService.createFlight(flightRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /** Get all flights */
  @GetMapping
  public ResponseEntity<List<Flight>> getAllFlights() {
    List<Flight> flights = flightApplicationService.getAllFlights();
    return ResponseEntity.ok(flights);
  }

  /** Get flight by flight number */
  @GetMapping("/{flightNumber}")
  public ResponseEntity<Flight> getFlight(@PathVariable String flightNumber) {
    try {
      Flight flight = flightApplicationService.getFlight(flightNumber);
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
      flightApplicationService.addPassenger(flightNumber, passengerRequest);
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
      boolean removed = flightApplicationService.removePassenger(flightNumber, passengerId);
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
    List<Flight> flights = flightApplicationService.findFlightsByRoute(origin, destination);
    return ResponseEntity.ok(flights);
  }

  /** Get flights by departure time range */
  @GetMapping("/departures")
  public ResponseEntity<List<Flight>> getFlightsByDepartureRange(
      @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
    List<Flight> flights = flightApplicationService.findFlightsByDepartureRange(start, end);
    return ResponseEntity.ok(flights);
  }

  /** Delete flight */
  @DeleteMapping("/{flightNumber}")
  public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
    try {
      flightApplicationService.deleteFlight(flightNumber);
      return ResponseEntity.ok("Flight deleted successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
