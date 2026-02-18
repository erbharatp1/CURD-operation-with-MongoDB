package com.bharat.airport.domain.model;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "passengers")
public class Passenger {
  @Id private UUID id;

  @NotBlank(message = "Passenger name is required")
  private String name;

  private SeatAssignment seatAssignment;

  public Passenger(String name, SeatAssignment seatAssignment) {
    this.name = name;
    this.seatAssignment = seatAssignment;
  }
}
