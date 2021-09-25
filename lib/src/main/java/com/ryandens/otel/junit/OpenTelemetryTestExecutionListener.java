package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * A {@link TestExecutionListener} that starts and stops a {@link io.opentelemetry.api.trace.Span}
 * for each {@link TestPlan} and for each {@link TestIdentifier}, in order to visualize and observe
 * the behavior of a test suite
 */
public final class OpenTelemetryTestExecutionListener implements TestExecutionListener {

  private final Tracer tracer;
  private final TracerProvider tracerProvider;
  private final ConcurrentHashMap<String, Span> testSpans;
  private final AtomicReference<Span> testPlanSpan = new AtomicReference<>();

  public OpenTelemetryTestExecutionListener(Tracer tracer, TracerProvider tracerProvider) {
    this.tracer = tracer;
    this.tracerProvider = tracerProvider;
    testSpans = new ConcurrentHashMap<>(32);
  }

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    testPlanSpan.set(
        tracer
            .spanBuilder("junit-test-suite")
            .setAttribute("junit.contains.tests", testPlan.containsTests())
            .setAttribute(
                "junit.engine.names",
                testPlan.getRoots().stream()
                    .map(TestIdentifier::getDisplayName)
                    .collect(Collectors.joining(", ")))
            .startSpan());
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    testPlanSpan.getAndSet(null).end();
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
