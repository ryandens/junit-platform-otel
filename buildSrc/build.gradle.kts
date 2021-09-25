plugins {
  `kotlin-dsl`
}

repositories {  gradlePluginPortal()
}

dependencies {
  implementation("com.diffplug.spotless:spotless-plugin-gradle:5.15.1")
  implementation("com.netflix.nebula", "nebula-publishing-plugin", "18.0.0")
}