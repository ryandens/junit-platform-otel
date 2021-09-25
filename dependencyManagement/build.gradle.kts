plugins {
    `java-platform`
    id("com.ryandens.base")
}

javaPlatform {
    allowDependencies() // allowed so we can depend on BOMs
}

dependencies {
    api(enforcedPlatform("org.junit:junit-bom:5.8.1"))
}
