plugins {
  id("nebula.publish-verification") version "18.0.0" apply false
}

// spotless needs to be able to resolve dependencies on the root project for some reason
buildscript {
  repositories {
    mavenCentral()
  }
}
