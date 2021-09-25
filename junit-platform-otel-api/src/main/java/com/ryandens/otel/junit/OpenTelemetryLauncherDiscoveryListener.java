package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.EngineDiscoveryResult;
import org.junit.platform.launcher.LauncherDiscoveryListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;

/**
 * A {@link LauncherDiscoveryListener} that traces the discovery of the JUnit {@link
 * org.junit.platform.launcher.Launcher} and the JUnit {@link org.junit.platform.engine.TestEngine}
 * with OpenTelemetry {@link io.opentelemetry.api.trace.Span}s in order to give JUnit the ability to
 * understand how and when this process takes place
 */
public final class OpenTelemetryLauncherDiscoveryListener implements LauncherDiscoveryListener {

  private final Tracer tracer;
  private final AtomicReference<Span> launcherDiscoverySpan = new AtomicReference<>();
  private final Map<UniqueId, Span> engineDiscoverySpans = new ConcurrentHashMap<>();

  public OpenTelemetryLauncherDiscoveryListener(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public void launcherDiscoveryStarted(LauncherDiscoveryRequest request) {
    launcherDiscoverySpan.set(
        tracer
            .spanBuilder("junit-launcher-discovery")
            .setAttribute(
                "junit.discovery.launcher.selectors",
                request.getSelectorsByType(DiscoverySelector.class).size())
            .setAttribute(
                "junit.discovery.launcher.filters",
                request.getFiltersByType(DiscoveryFilter.class).size())
            .startSpan());
  }

  @Override
  public void launcherDiscoveryFinished(LauncherDiscoveryRequest request) {
    launcherDiscoverySpan.getAndSet(null).end();
  }

  @Override
  public void engineDiscoveryStarted(UniqueId engineId) {
    engineDiscoverySpans.put(
        engineId,
        tracer
            .spanBuilder("junit-engine-discovery")
            .setAttribute("junit.discovery.engine.id", engineId.toString())
            .startSpan());
  }

  @Override
  public void engineDiscoveryFinished(UniqueId engineId, EngineDiscoveryResult result) {
    final Span span =
        engineDiscoverySpans
            .remove(engineId)
            .setAttribute("junit.discovery.engine.result", result.getStatus().toString());
    result
        .getThrowable()
        .ifPresent(t -> span.setAttribute("junit.discovery.engine.result.message", t.getMessage()));
    span.end();
  }
}
