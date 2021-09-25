package com.ryandens.otel.junit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

final class LibraryTest {
  @Test
  void someLibraryMethodReturnsTrue() {
    Library classUnderTest = new Library();
    assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'");
  }
}
