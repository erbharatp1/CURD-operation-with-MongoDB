package com.bharat.airport.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bharat.airport.domain.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;

class DataInitializerTest {

  @Test
  void shouldInitializeDataWhenRepositoryIsEmpty() throws Exception {
    FlightRepository repository = mock(FlightRepository.class);
    when(repository.count()).thenReturn(0L);

    DataInitializer initializer = new DataInitializer();
    CommandLineRunner runner = initializer.initData(repository);
    runner.run();

    verify(repository, times(3)).save(any());
  }

  @Test
  void shouldNotInitializeDataWhenRepositoryIsNotEmpty() throws Exception {
    FlightRepository repository = mock(FlightRepository.class);
    when(repository.count()).thenReturn(5L);

    DataInitializer initializer = new DataInitializer();
    CommandLineRunner runner = initializer.initData(repository);
    runner.run();

    verify(repository, never()).save(any());
  }
}
