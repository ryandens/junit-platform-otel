plugins {
    id("com.ryandens.java-library-publish-conventions")
}

description = """
  Module which auto-registers the com.ryandens:junit-platform-otel-api modules 
  with the JUnit Engine and uses the OpenTelemetry auto-configured SDK for 
  tracing.
""".trimIndent()

dependencies {
    val autoServiceVersion = "1.0"
    compileOnly("com.google.auto.service", "auto-service-annotations", autoServiceVersion)
    annotationProcessor("com.google.auto.service", "auto-service", autoServiceVersion)
    api(project(":junit-platform-otel-api"))
    api("org.junit.platform:junit-platform-launcher")
    api("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    api("io.opentelemetry:opentelemetry-exporter-otlp-trace")
    api("io.grpc:grpc-netty-shaded:1.39.0")
}
