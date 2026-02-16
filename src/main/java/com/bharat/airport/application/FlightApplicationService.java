package com.bharat.airport.application;

import com.bharat.airport.application.dto.FlightRequest;
import com.bharat.airport.application.dto.PassengerRequest;
import com.bharat.airport.domain.model.Flight;
import com.bharat.airport.domain.model.Passenger;
import com.bharat.airport.domain.model.SeatAssignment;
import com.bharat.airport.domain.repository.FlightRepository;
import com.bharat.airport.domain.service.FlightService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FlightApplicationService {

  private final FlightService flightService;
  private final FlightRepository flightRepository;

  public FlightApplicationService(FlightService flightService, FlightRepository flightRepository) {
    this.flightService = flightService;
    this.flightRepository = flightRepository;
  }

  public Flight createFlight(FlightRequest flightRequest) {
    Flight flight =
        new Flight(
            flightRequest.getFlightNumber(),
            flightRequest.getOrigin(),
            flightRequest.getDestination(),
            flightRequest.getScheduledDeparture(),
            flightRequest.getScheduledArrival());
    return flightRepository.save(flight);
  }

  public List<Flight> getAllFlights() {
    return flightRepository.findAll();
  }

  public Flight getFlight(String flightNumber) {
    return flightService.getFlightWithPassengers(flightNumber);
  }

  public void addPassenger(String flightNumber, PassengerRequest passengerRequest) {
    SeatAssignment seatAssignment = null;
    if (passengerRequest.getSeatNumber() != null && passengerRequest.getSeatClass() != null) {
      seatAssignment =
          new SeatAssignment(passengerRequest.getSeatNumber(), passengerRequest.getSeatClass());
    }

    Passenger passenger =
        new Passenger(UUID.randomUUID(), passengerRequest.getName(), seatAssignment);
    flightService.addPassengerToFlight(flightNumber, passenger);
  }

  public boolean removePassenger(String flightNumber, String passengerId) {
    return flightService.removePassengerFromFlight(flightNumber, passengerId);
  }

  public List<Flight> findFlightsByRoute(String origin, String destination) {
    return flightService.findFlightsByRoute(origin, destination);
  }

  public List<Flight> findFlightsByDepartureRange(LocalDateTime start, LocalDateTime end) {
    return flightService.findFlightsByDepartureRange(start, end);
  }

  public void deleteFlight(String flightNumber) {
    if (flightService.flightExists(flightNumber)) {
      flightRepository.deleteById(flightNumber);
    } else {
      throw new IllegalArgumentException("Flight not found: " + flightNumber);
    }
  }
}
