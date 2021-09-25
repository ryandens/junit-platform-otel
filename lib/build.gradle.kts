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
    compileOnly("org.junit.platform:junit-platform-launcher:1.8.0")
    testImplementation(platform("org.junit:junit-bom:5.8.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}
