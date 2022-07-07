import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish.base")
}

tasks.withType<KotlinCompile>()
  .configureEach {
    kotlinOptions {
      val toAdd = mutableListOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.contracts.ExperimentalContracts",
        "-Xjvm-default=enable"
      )
      if (name.contains("test", true)) {
        toAdd += "-Xopt-in=kotlin.ExperimentalStdlibApi"
      }
      @Suppress("SuspiciousCollectionReassignment") // It's not suspicious
      freeCompilerArgs += toAdd
    }
  }

dependencies {
  // So the j16 source set can "see" main Moshi sources
  compileOnly(libs.jsr305)
  api(libs.okio)

  testCompileOnly(libs.jsr305)
  testImplementation(libs.junit)
  testImplementation(libs.truth)
}
configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
  configure(com.vanniktech.maven.publish.KotlinJvm(javadocJar = com.vanniktech.maven.publish.JavadocJar.None()))
}
