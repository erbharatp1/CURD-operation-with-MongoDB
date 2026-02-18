package com.bharat.airport.application;

import com.bharat.airport.application.dto.AirportRequest;
import com.bharat.airport.domain.exception.AirportAlreadyExistsException;
import com.bharat.airport.domain.exception.AirportNotFoundException;
import com.bharat.airport.domain.model.Airport;
import com.bharat.airport.domain.repository.AirportRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

  @CacheEvict(value = "airports", allEntries = true)
  public Airport registerAirport(AirportRequest airportRequest) {
    if (airportRepository.existsById(airportRequest.getCode())) {
      log.error("Airport already exists with code {}", airportRequest.getCode());
      throw new AirportAlreadyExistsException(airportRequest.getCode());
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
    return airportRepository.findById(code).orElseThrow(() -> new AirportNotFoundException(code));
  }

  @Cacheable(value = "airports")
  public List<Airport> getAllAirports() {
    return airportRepository.findAll();
  }

  @CacheEvict(value = "airports", allEntries = true)
  public void deleteAirport(String code) {
    if (!airportRepository.existsById(code)) {
      log.error("Airport not found with code {}", code);
      throw new AirportNotFoundException(code);
    }
    airportRepository.deleteById(code);
    log.info("Airport deleted successfully: {}", code);
  }
}
