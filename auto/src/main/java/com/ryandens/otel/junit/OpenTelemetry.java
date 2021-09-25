package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.OpenTelemetrySdkAutoConfiguration;

public enum OpenTelemetry {
  SINGLETON;

  public final Tracer tracer;
  public final TracerProvider tracerProvider;

  OpenTelemetry() {
    final OpenTelemetrySdk otelSdk = OpenTelemetrySdkAutoConfiguration.initialize(false);
    tracer = otelSdk.getTracer("junit-extension");
    tracerProvider = otelSdk.getTracerProvider();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        otelSdk.getSdkTracerProvider().forceFlush();
      }
    }));
  }
}
