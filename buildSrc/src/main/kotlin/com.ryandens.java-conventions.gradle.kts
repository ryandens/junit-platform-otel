plugins {
  java
  id("com.ryandens.base")
}

repositories {
  mavenCentral()
}

spotless {
  java {
    googleJavaFormat()
  }
}

val dependencyManagement by configurations.creating {
  isCanBeConsumed = false
  isCanBeResolved = false
  isVisible = false
}

dependencies {
  dependencyManagement(platform(project(":dependencyManagement")))
  afterEvaluate {
    configurations.configureEach {
      if (isCanBeResolved && !isCanBeConsumed) {
        extendsFrom(dependencyManagement)
      }
    }
  }
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile> {
  options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-processing", "-Xlint:-serial", "-Werror"))
  options.release.set(11)
}

tasks.test {
  useJUnitPlatform()
}