plugins {
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

// spotless needs to be able to resolve dependencies on the root project for some reason
buildscript {
  repositories {
    mavenCentral()
  }
}
group = "com.ryandens"
version = "0.1.0"

nexusPublishing {
  repositories {
    sonatype {
      stagingProfileId.set("31cb749c34629")
    }
  }
}