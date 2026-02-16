package com.bharat.airport.application.dto;

import com.bharat.airport.domain.model.SeetClass;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequest {
  @NotBlank(message = "Passenger name is required")
  private String name;

  private String seatNumber;

  private SeetClass seatClass; // Economy, Business, First
}
