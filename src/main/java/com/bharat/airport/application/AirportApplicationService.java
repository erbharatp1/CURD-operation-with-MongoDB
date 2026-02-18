package com.bharat.airport.application;

import com.bharat.airport.application.dto.AirportRequest;
import com.bharat.airport.domain.model.Airport;
import com.bharat.airport.domain.repository.AirportRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AirportApplicationService {

  private final AirportRepository airportRepository;

  public AirportApplicationService(AirportRepository airportRepository) {
    this.airportRepository = airportRepository;
  }

  public Airport registerAirport(AirportRequest airportRequest) {
    if (airportRepository.existsById(airportRequest.getCode())) {
      throw new IllegalArgumentException("Airport already exists: " + airportRequest.getCode());
    }
    Airport airport =
        Airport.builder()
            .code(airportRequest.getCode())
            .name(airportRequest.getName())
            .createdAt(LocalDateTime.now())
            .createdBy("system")
            .updatedAt(LocalDateTime.now())
            .isEnabled(airportRequest.isEnabled())
            .build();
    return airportRepository.save(airport);
  }

  public Airport getAirport(String code) {
    return airportRepository
        .findById(code)
        .orElseThrow(() -> new IllegalArgumentException("Airport not found: " + code));
  }

  public List<Airport> getAllAirports() {
    return airportRepository.findAll();
  }

  public void deleteAirport(String code) {
    if (!airportRepository.existsById(code)) {
      throw new IllegalArgumentException("Airport not found: " + code);
    }
    airportRepository.deleteById(code);
    log.info("Airport deleted successfully: {}", code);
  }
}
