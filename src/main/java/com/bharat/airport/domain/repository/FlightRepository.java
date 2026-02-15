package com.bharat.airport.domain.repository;
  
import com.bharat.airport.domain.model.Flight;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {

  Optional<Flight> findByFlightNumber(String flightNumber);

  List<Flight> findByOrigin(String origin);

  List<Flight> findByDestination(String destination);

  @Query("{'scheduledDeparture': {$gte: ?0, $lte: ?1}}")
  List<Flight> findFlightsByDepartureTimeRange(LocalDateTime start, LocalDateTime end);

  @Query("{'origin': ?0, 'destination': ?1}")
  List<Flight> findByRoute(String origin, String destination);
}
