package com.bharat.airport.interfaces.web;

import com.bharat.airport.application.FlightApplicationService;
import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flights", description = "Operations related to flight management")
public class FlightController {

  private final FlightApplicationService flightApplicationService;

  public FlightController(FlightApplicationService flightApplicationService) {
    this.flightApplicationService = flightApplicationService;
  }

  /** Create a new flight */
  @PostMapping
  @Operation(summary = "Create a new flight")
  public ResponseEntity<Flight> createFlight(@Valid @RequestBody FlightRequest flightRequest) {
    Flight savedFlight = flightApplicationService.createFlight(flightRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
  }

  /** Get all flights */
  @GetMapping
  @Operation(summary = "Get all flights")
  public ResponseEntity<List<Flight>> getAllFlights() {
    List<Flight> flights = flightApplicationService.getAllFlights();
    return ResponseEntity.ok(flights);
  }

  /** Get flight by flight number */
  @GetMapping("/{flightNumber}")
  @Operation(summary = "Get flight by flight number")
  public ResponseEntity<Flight> getFlight(@PathVariable String flightNumber) {
    Flight flight = flightApplicationService.getFlight(flightNumber);
    return ResponseEntity.ok(flight);
  }

  /** Add passenger to flight */
  @PostMapping("/{flightNumber}/passengers")
  @Operation(summary = "Add passenger to a flight")
  public ResponseEntity<String> addPassenger(
      @PathVariable String flightNumber, @Valid @RequestBody PassengerRequest passengerRequest) {
    flightApplicationService.addPassenger(flightNumber, passengerRequest);
    return ResponseEntity.ok("Passenger added successfully");
  }

  /** Remove passenger from flight */
  @DeleteMapping("/{flightNumber}/passengers/{passengerId}")
  @Operation(summary = "Remove passenger from a flight")
  public ResponseEntity<String> removePassenger(
      @PathVariable String flightNumber, @PathVariable String passengerId) {
    boolean removed = flightApplicationService.removePassenger(flightNumber, passengerId);
    if (removed) {
      return ResponseEntity.ok("Passenger removed successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /** Get flights by route */
  @GetMapping("/route")
  @Operation(summary = "Find flights by origin and destination")
  public ResponseEntity<List<Flight>> getFlightsByRoute(
      @RequestParam String origin, @RequestParam String destination) {
    List<Flight> flights = flightApplicationService.findFlightsByRoute(origin, destination);
    return ResponseEntity.ok(flights);
  }

  /** Get flights by departure time range */
  @GetMapping("/departures")
  @Operation(summary = "Find flights by departure time range")
  public ResponseEntity<List<Flight>> getFlightsByDepartureRange(
      @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
    List<Flight> flights = flightApplicationService.findFlightsByDepartureRange(start, end);
    return ResponseEntity.ok(flights);
  }

  /** Delete flight */
  @DeleteMapping("/{flightNumber}")
  @Operation(summary = "Delete a flight")
  public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
    flightApplicationService.deleteFlight(flightNumber);
    return ResponseEntity.ok("Flight deleted successfully");
  }
}
