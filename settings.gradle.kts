dependencyResolutionManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "flink-demo"
include("protos")
include("flink-demo")
