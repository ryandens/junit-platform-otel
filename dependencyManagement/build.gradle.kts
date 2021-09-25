plugins {
    `java-platform`
    id("com.ryandens.base")
}

javaPlatform {
    allowDependencies() // allowed so we can depend on BOMs
}

dependencies {
    api(enforcedPlatform("org.junit:junit-bom:5.8.1"))
    api(platform("io.opentelemetry:opentelemetry-bom:1.6.0"))
    api(platform("io.opentelemetry:opentelemetry-bom-alpha:1.6.0-alpha"))

    constraints {
        // specifying versions for libraries without BOMs go here
    }
}
