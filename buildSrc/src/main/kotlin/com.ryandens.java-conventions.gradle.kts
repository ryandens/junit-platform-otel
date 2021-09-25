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
  val junitVersion = "5.8.1"
  compileOnly(enforcedPlatform("org.junit:junit-bom:$junitVersion"))
  testImplementation(enforcedPlatform("org.junit:junit-bom:$junitVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
}