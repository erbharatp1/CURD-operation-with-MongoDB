package com.bharat.airport.application.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.bharat.airport.domain.model.SeetClass;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DtoTest {

  @Test
  void testFlightRequest() {
    FlightRequest request = new FlightRequest();
    request.setFlightNumber("F1");
    request.setOrigin("O");
    request.setDestination("D");
    LocalDateTime dep = LocalDateTime.now().plusHours(1);
    LocalDateTime arr = LocalDateTime.now().plusHours(2);
    request.setScheduledDeparture(dep);
    request.setScheduledArrival(arr);

    assertEquals("F1", request.getFlightNumber());
    assertEquals("O", request.getOrigin());
    assertEquals("D", request.getDestination());
    assertEquals(dep, request.getScheduledDeparture());
    assertEquals(arr, request.getScheduledArrival());

    FlightRequest request2 = new FlightRequest("F2", "O2", "D2", dep, arr);
    assertEquals("F2", request2.getFlightNumber());
  }

  @Test
  void testPassengerRequest() {
    PassengerRequest request = new PassengerRequest();
    request.setName("John");
    request.setSeatNumber("1A");
    request.setSeatClass(SeetClass.Business);

    assertEquals("John", request.getName());
    assertEquals("1A", request.getSeatNumber());
    assertEquals(SeetClass.Business, request.getSeatClass());

    PassengerRequest request2 = new PassengerRequest("Jane", "1B", SeetClass.Economy);
    assertEquals("Jane", request2.getName());
  }
}
