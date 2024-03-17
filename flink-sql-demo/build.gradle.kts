import flink_demo.Dependencies

plugins {
  application
  id("com.github.johnrengelman.shadow")
}

application {
  mainClass.set("")
}

dependencies {
  implementation(project(":protos"))
  runtimeOnly(Dependencies.flinkConnectorKafka)
  runtimeOnly(Dependencies.flinkSQLKafkaConnector)
  runtimeOnly(Dependencies.flinkProtobuf)
}

val shadowJar by tasks.getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
  exclude("module-info.class") // https://github.com/johnrengelman/shadow/issues/352
  archiveClassifier.set(null as String?)
}
