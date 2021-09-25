plugins {
  java
  id("com.google.cloud.tools.jib") version "3.1.4"
}

repositories {
  mavenCentral()
}

jib.from.image = "eclipse-temurin:17_35-jdk-focal"

jib.container.entrypoint = listOf("java", "-jar", "/app/libs/junit-platform-console-standalone-1.8.0.jar", "--classpath", "/app/classes:/app/extra", "--scan-classpath")

dependencies {
  implementation(platform("org.junit:junit-bom:5.8.0"))
  compileOnly("org.junit.jupiter:junit-jupiter-api")
  implementation("org.junit.platform:junit-platform-console-standalone:1.8.0")
}
