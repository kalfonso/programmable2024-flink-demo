import flink_demo.Dependencies

plugins {
  application
  id("com.github.johnrengelman.shadow")
}

application {
  mainClass.set("")
}

dependencies {
  runtimeOnly(project(":protos"))
  runtimeOnly("com.google.protobuf:protobuf-java:3.21.2")
}

val shadowJar by tasks.getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
  exclude("module-info.class") // https://github.com/johnrengelman/shadow/issues/352
  archiveClassifier.set(null as String?)
}
