plugins {
  id("com.diffplug.spotless")
}

spotless {
  kotlinGradle {
    ktlint()
  }
}
