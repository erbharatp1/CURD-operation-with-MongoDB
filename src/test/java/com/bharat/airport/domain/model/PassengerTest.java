package com.bharat.airport.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class PassengerTest {

  @Test
  void shouldSetAndGetPassengerProperties() {
    Passenger p = new Passenger();
    UUID id = UUID.randomUUID();
    p.setId(id);
    p.setName("John");
    SeatAssignment seat = new SeatAssignment("1A", SeetClass.Business);
    p.setSeatAssignment(seat);

    assertEquals(id, p.getId());
    assertEquals("John", p.getName());
    assertEquals(seat, p.getSeatAssignment());
  }

  @Test
  void shouldConstructWithAllFields() {
    UUID id = UUID.randomUUID();
    SeatAssignment seat = new SeatAssignment("1A", SeetClass.Business);
    Passenger p = new Passenger(id, "John", seat);

    assertEquals(id, p.getId());
    assertEquals("John", p.getName());
    assertEquals(seat, p.getSeatAssignment());
  }
}
