plugins {
    `java-library`
    id("com.diffplug.spotless") version "5.15.1"
}

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
    compileOnly("org.junit.platform:junit-platform-launcher:1.8.0")
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.6.0"))
    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:1.6.0-alpha"))
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
}
