package com.bharat.airport.interfaces.web.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class ExceptionTest {

  @Test
  void testBadRequestException() {
    BadRequestException ex = new BadRequestException("error");
    assertEquals("error", ex.getMessage());
    assertEquals("error", ex.getErrorMessage());
  }

  @Test
  void testResourceNotFoundException() {
    ResourceNotFoundException ex = new ResourceNotFoundException("error");
    assertEquals("error", ex.getMessage());
    assertEquals("error", ex.getErrorMessage());
  }

  @Test
  void testResourceAlreadyExistsException() {
    ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("error");
    assertEquals("error", ex.getMessage());
    assertEquals("error", ex.getErrorMessage());
  }

  @Test
  void testInvalidRequestException() {
    InvalidRequestException ex = new InvalidRequestException("field", "error");
    assertEquals("field", ex.getField());
    assertEquals("error", ex.getErrorMessage());
  }

  @Test
  void testTriggerGlobalExceptionHandlerPaths() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // MethodArgumentNotValidException
    org.springframework.web.bind.MethodArgumentNotValidException manve =
        mock(org.springframework.web.bind.MethodArgumentNotValidException.class);
    org.springframework.validation.BindingResult br =
        mock(org.springframework.validation.BindingResult.class);
    when(manve.getBindingResult()).thenReturn(br);
    when(br.getFieldErrors()).thenReturn(java.util.Collections.emptyList());
    assertNotNull(handler.handleValidationExceptions(manve));

    // HandlerMethodValidationException
    org.springframework.web.method.annotation.HandlerMethodValidationException hmve =
        mock(org.springframework.web.method.annotation.HandlerMethodValidationException.class);
    when(hmve.getAllErrors()).thenReturn(java.util.Collections.emptyList());
    assertNotNull(handler.handleValidationExceptions(hmve));

    // HttpMessageNotReadableException
    org.springframework.http.converter.HttpMessageNotReadableException hmnre =
        mock(org.springframework.http.converter.HttpMessageNotReadableException.class);
    assertNotNull(handler.handleMissingRequestBody(hmnre));

    // InvalidRequestException
    assertNotNull(handler.handleInvalidRequestException(new InvalidRequestException("f", "m")));

    // BadRequestException
    assertNotNull(handler.handleInvalidRequestException(new BadRequestException("m")));

    // Exception
    assertNotNull(handler.handleGenericException(new Exception("m")));

    // IllegalArgumentException paths
    assertNotNull(
        handler.handleIllegalArgumentException(new IllegalArgumentException("not found")));
    assertNotNull(
        handler.handleIllegalArgumentException(new IllegalArgumentException("already assigned")));
    assertNotNull(handler.handleIllegalArgumentException(new IllegalArgumentException("other")));

    // MissingServletRequestPartException
    org.springframework.web.multipart.support.MissingServletRequestPartException msrpe =
        mock(org.springframework.web.multipart.support.MissingServletRequestPartException.class);
    assertNotNull(handler.handleMissingImage(msrpe));
  }
}
