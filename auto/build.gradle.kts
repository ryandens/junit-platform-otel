plugins {
    `java-library`
    id("com.diffplug.spotless")
    id("nebula.maven-publish")
}

group = "com.ryandens"
version = "0.1.0"

repositories {
    mavenCentral()
}

spotless {
    java {
        googleJavaFormat()
    }
    kotlinGradle {
        ktlint()
    }
}

dependencies {
    compileOnly("com.google.auto.service", "auto-service-annotations", "1.0")
    annotationProcessor("com.google.auto.service", "auto-service", "1.0")
    api(project(":lib"))
    api("org.junit.platform:junit-platform-launcher:1.8.0")
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.6.0"))
    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:1.6.0-alpha"))
    api("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    api("io.opentelemetry:opentelemetry-exporter-otlp-trace")
    api("io.grpc:grpc-netty-shaded:1.39.0")
}
