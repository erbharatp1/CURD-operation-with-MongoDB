package com.bharat.airport.application.dto;
  
import com.bharat.airport.domain.model.SeetClass;
import jakarta.validation.constraints.NotBlank;

public class PassengerRequest {
  @NotBlank(message = "Passenger name is required")
  private String name;

  private String seatNumber;

  private SeetClass seatClass; // Economy, Business, First

  public PassengerRequest() {}

  public PassengerRequest(String name, String seatNumber, SeetClass seatClass) {
    this.name = name;
    this.seatNumber = seatNumber;
    this.seatClass = seatClass;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(String seatNumber) {
    this.seatNumber = seatNumber;
  }

  public SeetClass getSeatClass() {
    return seatClass;
  }

  public void setSeatClass(SeetClass seatClass) {
    this.seatClass = seatClass;
  }
}
