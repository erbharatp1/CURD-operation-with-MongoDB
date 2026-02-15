package com.bharat.airport.repository;

import com.bharat.airport.domain.Passenger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, String> {

  List<Passenger> findByName(String name);

  @Query("{'seatAssignment.seatNumber': ?0}")
  Optional<Passenger> findBySeatNumber(String seatNumber);

  @Query("{'seatAssignment.seatClass': ?0}")
  List<Passenger> findBySeatClass(String seatClass);
}
