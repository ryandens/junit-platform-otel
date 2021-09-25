plugins {
    id("com.ryandens.java-library-conventions")
    id("nebula.maven-publish")
}

dependencies {
    compileOnly("io.opentelemetry:opentelemetry-api")
    compileOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
    testImplementation("org.junit.platform:junit-platform-testkit")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("testkit")
    }
}
