plugins {
  id("com.ryandens.java-conventions")
}

dependencies {
  testRuntimeOnly(project(":auto"))
}

tasks.test {
  // This project  ignores failures because meant to be used to be used as a simple example project
  ignoreFailures = true
}