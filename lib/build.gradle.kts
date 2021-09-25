plugins {
    id("com.ryandens.java-library-conventions")
    id("nebula.maven-publish")
}

dependencies {
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.6.0"))
    compileOnly("io.opentelemetry:opentelemetry-api")
    compileOnly("org.junit.platform:junit-platform-launcher:1.8.0")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
    testImplementation("org.junit.platform:junit-platform-testkit")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("testkit")
    }
}
