package com.bharat.airport.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "passengers")
public class Passenger {
  @Id private UUID id;

  @NotBlank(message = "Passenger name is required")
  private String name;

  private SeatAssignment seatAssignment;

  public Passenger() {}

  public Passenger(String name, SeatAssignment seatAssignment) {
    this.name = name;
    this.seatAssignment = seatAssignment;
  }

  public Passenger(UUID id, String name, SeatAssignment seatAssignment) {
    this.id = id;
    this.name = name;
    this.seatAssignment = seatAssignment;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SeatAssignment getSeatAssignment() {
    return seatAssignment;
  }

  public void setSeatAssignment(SeatAssignment seatAssignment) {
    this.seatAssignment = seatAssignment;
  }
}
