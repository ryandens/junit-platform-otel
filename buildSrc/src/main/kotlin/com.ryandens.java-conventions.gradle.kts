plugins {
  java
  id("com.diffplug.spotless")
}

repositories {
  mavenCentral()
}

spotless {
  java {
    googleJavaFormat()
  }
  kotlinGradle {
    ktlint()
  }
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.8.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
}