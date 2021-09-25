plugins {
    `java-library`
    id("com.diffplug.spotless") version "5.15.1"
}

spotless {
    java {
        googleJavaFormat()
    }
    kotlinGradle {
        ktlint()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.6.0"))
    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:1.6.0-alpha"))
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    compileOnly("org.junit.platform:junit-platform-launcher:1.8.0")
    testImplementation(platform("org.junit:junit-bom:5.8.0"))
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
    testImplementation("org.junit.platform:junit-platform-testkit")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}
