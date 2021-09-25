package com.ryandens.otel.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
import io.opentelemetry.sdk.trace.data.StatusData;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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
    openTelemetryTestExecutionListener = new OpenTelemetryTestExecutionListener(tracer);
    openTelemetryLauncherDiscoveryListener = new OpenTelemetryLauncherDiscoveryListener(tracer);
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

    assertEquals(1, failedCount.get());
    assertEquals(2, successCount.get());

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
                                    "JUnit Jupiter")),
                    spanAssert ->
                        spanAssert
                            .hasName("JUnit Jupiter Test Container")
                            .hasStatus(StatusData.ok())
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.container.name"),
                                    "JUnit Jupiter",
                                    AttributeKey.stringKey("junit.unique.id"),
                                    "[engine:junit-jupiter]",
                                    AttributeKey.stringKey("junit.status"),
                                    "SUCCESSFUL")),
                    spanAssert ->
                        spanAssert
                            .hasName("JUnit Jupiter Test Container")
                            .hasStatus(StatusData.ok())
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.container.name"),
                                    "OpenTelemetryTestExecutionListenerTest$ExampleFoo",
                                    AttributeKey.stringKey("junit.unique.id"),
                                    "[engine:junit-jupiter]/[class:com.ryandens.otel.junit.OpenTelemetryTestExecutionListenerTest$ExampleFoo]",
                                    AttributeKey.stringKey("junit.status"),
                                    "SUCCESSFUL")),
                    spanAssert ->
                        spanAssert
                            .hasName("Test")
                            .hasStatus(StatusData.ok())
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.test.name"),
                                    "fooLong()",
                                    AttributeKey.stringKey("junit.unique.id"),
                                    "[engine:junit-jupiter]/[class:com.ryandens.otel.junit.OpenTelemetryTestExecutionListenerTest$ExampleFoo]/[method:fooLong()]",
                                    AttributeKey.stringKey("junit.status"),
                                    "SUCCESSFUL")),
                    spanAssert ->
                        spanAssert
                            .hasName("Test")
                            .hasStatus(StatusData.ok())
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.test.name"),
                                    "fooA()",
                                    AttributeKey.stringKey("junit.unique.id"),
                                    "[engine:junit-jupiter]/[class:com.ryandens.otel.junit.OpenTelemetryTestExecutionListenerTest$ExampleFoo]/[method:fooA()]",
                                    AttributeKey.stringKey("junit.status"),
                                    "SUCCESSFUL")),
                    spanAssert ->
                        spanAssert
                            .hasName("Test")
                            .hasStatus(StatusData.error())
                            .hasAttributes(
                                Attributes.of(
                                    AttributeKey.stringKey("junit.test.name"),
                                    "fooB()",
                                    AttributeKey.stringKey("junit.unique.id"),
                                    "[engine:junit-jupiter]/[class:com.ryandens.otel.junit.OpenTelemetryTestExecutionListenerTest$ExampleFoo]/[method:fooB()]",
                                    AttributeKey.stringKey("junit.status"),
                                    TestExecutionResult.Status.FAILED.name(),
                                    AttributeKey.stringKey("junit.exception.message"),
                                    "failure!",
                                    AttributeKey.stringKey("junit.exception.class"),
                                    "org.opentest4j.AssertionFailedError"))));
  }

  @Tag("testkit")
  static final class ExampleFoo {

    @Test
    void fooA() {
      assertTrue(true);
    }

    @Test
    void fooB() {
      Assertions.fail("failure!");
    }

    @Test
    void fooLong() throws InterruptedException {
      Thread.sleep(2000);
    }
  }
}
