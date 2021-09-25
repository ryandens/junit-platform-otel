package com.ryandens.otel.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.EngineDiscoveryResult;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

final class OpenTelemetryTestExecutionListenerTest {

  @RegisterExtension static OpenTelemetryExtension otelTesting = OpenTelemetryExtension.create();
  private OpenTelemetryTestExecutionListener openTelemetryTestExecutionListener;
  private OpenTelemetryLauncherDiscoveryListener openTelemetryLauncherDiscoveryListener;

  @BeforeEach
  void beforeEach() {
    final Tracer tracer = otelTesting.getOpenTelemetry().getTracer("test-tracer");
    final TracerProvider tracerProvider = otelTesting.getOpenTelemetry().getTracerProvider();
    openTelemetryTestExecutionListener =
        new OpenTelemetryTestExecutionListener(tracer, tracerProvider);
    openTelemetryLauncherDiscoveryListener =
        new OpenTelemetryLauncherDiscoveryListener(tracer, tracerProvider);
  }

  @Test
  void testExecutionListener() {
    LauncherDiscoveryRequest discoveryRequest =
        LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectClass(ExampleFoo.class))
            .build();

    final JupiterTestEngine testEngine = new JupiterTestEngine();
    final AtomicInteger successCount = new AtomicInteger(0);
    final AtomicInteger failedCount = new AtomicInteger(0);

    LauncherConfig launcherConfig =
        LauncherConfig.builder()
            .enableTestEngineAutoRegistration(false)
            .enableLauncherSessionListenerAutoRegistration(false)
            .enableLauncherDiscoveryListenerAutoRegistration(false)
            .enablePostDiscoveryFilterAutoRegistration(false)
            .enableTestExecutionListenerAutoRegistration(false)
            .addTestExecutionListeners(openTelemetryTestExecutionListener)
            .addLauncherDiscoveryListeners(openTelemetryLauncherDiscoveryListener)
            .addTestExecutionListeners(
                new TestExecutionListener() {
                  @Override
                  public void executionFinished(
                      TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                    if (testIdentifier.isTest()) {
                      if (TestExecutionResult.Status.SUCCESSFUL.equals(
                          testExecutionResult.getStatus())) {
                        successCount.incrementAndGet();
                      } else {
                        failedCount.incrementAndGet();
                      }
                    }
                  }
                })
            .addLauncherDiscoveryListeners()
            .addTestEngines(testEngine)
            .build();

    try (LauncherSession session = LauncherFactory.openSession(launcherConfig)) {
      session.getLauncher().execute(discoveryRequest);
    }

    assertEquals(0, failedCount.get());
    assertEquals(3, successCount.get());

    otelTesting
        .assertTraces()
        .hasSize(3)
        .hasTracesSatisfyingExactly(
            traceAssert ->
                traceAssert.hasSpansSatisfyingExactly(
                    spanAssert ->
                        spanAssert
                            .hasName("junit-engine-discovery")
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.discovery.engine.result"),
                                    EngineDiscoveryResult.Status.SUCCESSFUL.name(),
                                    AttributeKey.stringKey("junit.discovery.engine.id"),
                                    "[engine:junit-jupiter]"))),
            traceAssert ->
                traceAssert.hasSpansSatisfyingExactly(
                    spanAssert ->
                        spanAssert
                            .hasName("junit-launcher-discovery")
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.longKey("junit.discovery.launcher.selectors"),
                                    1L,
                                    AttributeKey.longKey("junit.discovery.launcher.filters"),
                                    0L))),
            traceAssert ->
                traceAssert.hasSpansSatisfyingExactly(
                    spanAssert ->
                        spanAssert
                            .hasName("junit-test-suite")
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.booleanKey("junit.contains.tests"),
                                    true,
                                    AttributeKey.stringKey("junit.engine.names"),
                                    "JUnit Jupiter"))));
  }

  static final class ExampleFoo {

    @Test
    void fooA() {
      assertTrue(true);
    }

    @Test
    void fooB() {
      assertFalse(false);
    }

    @Test
    void fooLong() throws InterruptedException {
      Thread.sleep(3000);
    }
  }
}
