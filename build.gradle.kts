import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.23"
}

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    classpath(flink_demo.Dependencies.junitGradlePlugin)
    classpath(flink_demo.Dependencies.kotlinGradlePlugin)
    classpath(flink_demo.Dependencies.protobufGradlePlugin)
    classpath(flink_demo.Dependencies.shadowJarPlugin)
  }
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "kotlin")

  tasks
    .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>()
    .configureEach {
      compilerOptions
        .languageVersion
        .set(
          org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
        )
      compilerOptions
        .jvmTarget
        .set(JvmTarget.JVM_11)
    }

  kotlin {
    jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of("11"))
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
      events("started", "passed", "skipped", "failed")
      exceptionFormat = TestExceptionFormat.FULL
      showExceptions = true
      showStackTraces = true
    }
  }

  configurations.all {
    exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j-impl")
  }
}
