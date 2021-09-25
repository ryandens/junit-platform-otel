package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
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
  private final ConcurrentHashMap<String, Span> testSpans;
  private final AtomicReference<Span> testPlanSpan = new AtomicReference<>();

  public OpenTelemetryTestExecutionListener(Tracer tracer) {
    this.tracer = tracer;
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
    final String spanName;
    final String attributeName;
    if (testIdentifier.isTest()) {
      spanName = "Test";
      attributeName = "junit.test.name";
    } else if (testIdentifier.isContainer()) {
      spanName = "JUnit Jupiter Test Container";
      attributeName = "junit.container.name";
    } else {
      spanName = "Unknown";
      attributeName = "junit.unknown.name";
    }
    final Span parentSpan =
        testIdentifier.getParentId().map(testSpans::get).orElse(testPlanSpan.get());
    testSpans.put(
        testIdentifier.getUniqueId(),
        tracer
            .spanBuilder(spanName)
            .setParent(Context.current().with(parentSpan))
            .setAttribute(attributeName, testIdentifier.getDisplayName())
            .setAttribute("junit.unique.id", testIdentifier.getUniqueId())
            .startSpan());
  }

  @Override
  public void executionFinished(
      TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    final Span span =
        testSpans
            .remove(testIdentifier.getUniqueId())
            .setStatus(
                TestExecutionResult.Status.SUCCESSFUL.equals(testExecutionResult.getStatus())
                    ? StatusCode.OK
                    : StatusCode.ERROR)
            .setAttribute("junit.status", testExecutionResult.getStatus().name());
    if (testExecutionResult.getThrowable().isPresent()) {
      span.setAttribute(
          "junit.exception.class", testExecutionResult.getThrowable().get().getClass().getName());
      span.setAttribute(
          "junit.exception.message", testExecutionResult.getThrowable().get().getMessage());
    }
    span.end();
  }
}
