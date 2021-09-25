plugins {
  java
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  testRuntimeOnly(project(":auto"))
}

tasks.test {
  // This project  ignores failures because meant to be used to be used as a simple example project
  ignoreFailures = true
  useJUnitPlatform()
}