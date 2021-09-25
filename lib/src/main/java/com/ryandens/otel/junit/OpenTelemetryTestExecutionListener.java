package com.ryandens.otel.junit;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * A {@link TestExecutionListener} that starts and stops a span for each {@link TestPlan} and for
 * each {@link TestIdentifier}, in order to visualize and observe the behavior of a test suite
 */
public final class OpenTelemetryTestExecutionListener implements TestExecutionListener {
  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    TestExecutionListener.super.testPlanExecutionStarted(testPlan);
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    TestExecutionListener.super.testPlanExecutionFinished(testPlan);
  }

  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    TestExecutionListener.super.executionStarted(testIdentifier);
  }

  @Override
  public void executionFinished(
      TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);
  }
}
