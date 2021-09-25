plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.8.0"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}
