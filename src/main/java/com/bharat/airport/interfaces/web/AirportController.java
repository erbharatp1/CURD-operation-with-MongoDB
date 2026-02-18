package com.bharat.airport.interfaces.web;

import com.bharat.airport.application.AirportApplicationService;
import com.bharat.airport.application.dto.AirportRequest;
import com.bharat.airport.domain.model.Airport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airports")
@Tag(name = "Airports", description = "Operations related to airport management")
public class AirportController {

  private final AirportApplicationService airportApplicationService;

  public AirportController(AirportApplicationService airportApplicationService) {
    this.airportApplicationService = airportApplicationService;
  }

  @PostMapping
  @Operation(summary = "Register a new airport")
  public ResponseEntity<Airport> registerAirport(
      @Valid @RequestBody AirportRequest airportRequest) {
    Airport airport = airportApplicationService.registerAirport(airportRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(airport);
  }

  @GetMapping("/{code}")
  @Operation(summary = "Get airport by code")
  public ResponseEntity<Airport> getAirport(@PathVariable String code) {
    Airport airport = airportApplicationService.getAirport(code);
    return ResponseEntity.ok(airport);
  }

  @GetMapping
  @Operation(summary = "Get all airports")
  public ResponseEntity<List<Airport>> getAllAirports() {
    List<Airport> airports = airportApplicationService.getAllAirports();
    return ResponseEntity.ok(airports);
  }

  @DeleteMapping("/{code}")
  @Operation(summary = "Delete an airport")
  public ResponseEntity<String> deleteAirport(@PathVariable String code) {
    airportApplicationService.deleteAirport(code);
    return ResponseEntity.ok("Airport deleted successfully");
  }
}
