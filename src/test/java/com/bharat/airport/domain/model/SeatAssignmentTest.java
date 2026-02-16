package com.bharat.airport.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SeatAssignmentTest {

  @Test
  void shouldSetAndGetSeatAssignmentProperties() {
    SeatAssignment s = new SeatAssignment();
    s.setSeatNumber("12B");
    s.setSeatClass(SeetClass.Economy);

    assertEquals("12B", s.getSeatNumber());
    assertEquals(SeetClass.Economy, s.getSeatClass());
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    SeatAssignment s1 = new SeatAssignment("12B", SeetClass.Economy);
    SeatAssignment s2 = new SeatAssignment("12B", SeetClass.Economy);
    SeatAssignment s3 = new SeatAssignment("12C", SeetClass.Economy);

    assertEquals(s1, s2);
    assertEquals(s1.hashCode(), s2.hashCode());
    assertNotEquals(s1, s3);
    assertNotEquals(s1, null);
    assertNotEquals(s1, "string");
  }

  @Test
  void testEnumValues() {
    assertNotNull(SeetClass.valueOf("First"));
    assertEquals(3, SeetClass.values().length);
  }
}
