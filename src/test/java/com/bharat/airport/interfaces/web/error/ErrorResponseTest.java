package com.bharat.airport.interfaces.web.error;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

  @Test
  void testErrorResponse() {
    ErrorDetail detail = new ErrorDetail("CODE", "REASON");
    ErrorResponse response = new ErrorResponse(Collections.singletonList(detail));

    assertEquals(1, response.getErrors().size());
    assertEquals("CODE", response.getErrors().get(0).getCode());
    assertEquals("REASON", response.getErrors().get(0).getReason());
    assertNotNull(response.getErrors().get(0).getDatetime());

    // Test setters and ToString for coverage
    response.setErrors(null);
    assertNull(response.getErrors());
    assertNotNull(response.toString());

    detail.setCode("C2");
    detail.setReason("R2");
    detail.setDatetime("D2");
    assertEquals("C2", detail.getCode());
    assertEquals("R2", detail.getReason());
    assertEquals("D2", detail.getDatetime());
    assertNotNull(detail.toString());
  }
}
