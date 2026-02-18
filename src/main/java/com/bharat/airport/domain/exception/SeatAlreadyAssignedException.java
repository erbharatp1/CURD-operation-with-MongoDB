package com.bharat.airport.domain.exception;

public class SeatAlreadyAssignedException extends RuntimeException {
  public SeatAlreadyAssignedException(String seatNumber) {
    super("Seat " + seatNumber + " is already assigned");
  }
}
