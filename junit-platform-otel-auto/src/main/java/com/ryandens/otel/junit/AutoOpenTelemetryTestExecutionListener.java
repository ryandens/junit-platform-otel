package com.ryandens.otel.junit;

import com.google.auto.service.AutoService;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * Auto-registered {@link OpenTelemetryTestExecutionListener} via the {@link
 * java.util.ServiceLoader} API using the {@link io.opentelemetry.api.trace.Tracer} created and
 * configured by the {@link
 * io.opentelemetry.sdk.autoconfigure.OpenTelemetrySdkAutoConfiguration#initialize()} method
 *
 * @see OpenTelemetryTestExecutionListener
 * @see io.opentelemetry.sdk.autoconfigure.OpenTelemetrySdkAutoConfiguration
 */
@AutoService(TestExecutionListener.class)
public final class AutoOpenTelemetryTestExecutionListener implements TestExecutionListener {

  private final OpenTelemetryTestExecutionListener inner;

  /** Default constructor, used by {@link java.util.ServiceLoader#load(Class)} */
  public AutoOpenTelemetryTestExecutionListener() {
    inner = new OpenTelemetryTestExecutionListener(OpenTelemetry.SINGLETON.tracer);
  }

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    inner.testPlanExecutionStarted(testPlan);
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    inner.testPlanExecutionFinished(testPlan);
  }

  @Override
  public void dynamicTestRegistered(TestIdentifier testIdentifier) {
    inner.dynamicTestRegistered(testIdentifier);
  }

  @Override
  public void executionSkipped(TestIdentifier testIdentifier, String reason) {
    inner.executionSkipped(testIdentifier, reason);
  }

  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    inner.executionStarted(testIdentifier);
  }

  @Override
  public void executionFinished(
      TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    inner.executionFinished(testIdentifier, testExecutionResult);
  }

  @Override
  public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
    inner.reportingEntryPublished(testIdentifier, entry);
  }
}
