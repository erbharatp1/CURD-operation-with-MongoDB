package com.bharat.airport.domain;

import java.util.Objects;

public class SeatAssignment {
  private String seatNumber;
  private SeetClass seatClass; // e.g., Economy, Business, First

  public SeatAssignment() {}

  public SeatAssignment(String seatNumber, SeetClass seatClass) {
    this.seatNumber = seatNumber;
    this.seatClass = seatClass;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SeatAssignment that = (SeatAssignment) o;
    return Objects.equals(seatNumber, that.seatNumber) && Objects.equals(seatClass, that.seatClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seatNumber, seatClass);
  }
}
