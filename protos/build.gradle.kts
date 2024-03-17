import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import flink_demo.Dependencies

plugins {
  id("java")
  id("com.google.protobuf")
}

repositories {
    mavenCentral()
}

dependencies {
  implementation(Dependencies.protobufJava)
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
  main {
    java {
      srcDirs("build/generated/source/proto/main/java")
    }
  }
}

protobuf {
  protoc {
    artifact = flink_demo.Dependencies.protoc
  }
}