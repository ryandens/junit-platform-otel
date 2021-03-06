package com.ryandens.otel.junit;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.OpenTelemetrySdkAutoConfiguration;

enum OpenTelemetry {
  SINGLETON;

  final Tracer tracer;

  OpenTelemetry() {
    final OpenTelemetrySdk otelSdk = OpenTelemetrySdkAutoConfiguration.initialize(false);
    tracer = otelSdk.getTracer("junit-extension");
    Runtime.getRuntime()
        .addShutdownHook(new Thread(() -> otelSdk.getSdkTracerProvider().forceFlush()));
  }
}
