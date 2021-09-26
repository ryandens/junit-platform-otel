plugins {
  id("com.ryandens.java-library-conventions")
  id("nebula.maven-publish")
  id("nebula.source-jar")
  id("nebula.javadoc-jar")
  id("nebula.maven-apache-license")
  id("nebula.maven-scm")
  id("nebula.info")
  id("nebula.maven-developer")
  id("nebula.contacts")
  id("nebula.publish-verification")
  signing
}

// sign the publication produced by the Nebula MavenPublishPlugin
signing {
  sign(extensions.getByType<PublishingExtension>().publications.getByName("nebula"))
}


// typically, we would use this Nebula Contacts plugin here, but this works just as well and I couldn't get it to work
extensions.getByType<PublishingExtension>().publications.getByName<MavenPublication>("nebula") {
  pom.developers {
    developer {
      id.set("ryandens")
      roles.set(listOf("owner"))
      name.set("Ryan Dens")
    }
  }
}
