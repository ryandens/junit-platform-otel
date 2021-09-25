package com.ryandens.otel.example.suite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class ExampleTest {

  @Test
  public void simple() {
    assertEquals(2, 1+1);
  }

  @Test
  public void failure() {
    fail("Failed test!");
  }

  @Test
  public void longTest() throws InterruptedException {
    Thread.sleep(3000);
  }
}
