package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.SelectorResolutionResult;
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
  private final TracerProvider tracerProvider;

  public OpenTelemetryLauncherDiscoveryListener(Tracer tracer, TracerProvider tracerProvider) {
    this.tracer = tracer;
    this.tracerProvider = tracerProvider;
  }

  @Override
  public void launcherDiscoveryStarted(LauncherDiscoveryRequest request) {
    LauncherDiscoveryListener.super.launcherDiscoveryStarted(request);
  }

  @Override
  public void launcherDiscoveryFinished(LauncherDiscoveryRequest request) {
    LauncherDiscoveryListener.super.launcherDiscoveryFinished(request);
  }

  @Override
  public void engineDiscoveryStarted(UniqueId engineId) {
    LauncherDiscoveryListener.super.engineDiscoveryStarted(engineId);
  }

  @Override
  public void engineDiscoveryFinished(UniqueId engineId, EngineDiscoveryResult result) {
    LauncherDiscoveryListener.super.engineDiscoveryFinished(engineId, result);
  }

  @Override
  public void selectorProcessed(
      UniqueId engineId, DiscoverySelector selector, SelectorResolutionResult result) {
    LauncherDiscoveryListener.super.selectorProcessed(engineId, selector, result);
  }
}
