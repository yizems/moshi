import com.vanniktech.maven.publish.JavadocJar.Dokka
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.jvm.tasks.Jar

plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish.base")
}

dependencies {
  compileOnly(libs.jsr305)
  api(project(":moshi"))

  testImplementation(libs.junit)
  testImplementation(libs.truth)
}

tasks.withType<Jar>().configureEach {
  manifest {
    attributes("Automatic-Module-Name" to "com.squareup.moshi.adapters")
  }
}
configure<MavenPublishBaseExtension> {
  configure(KotlinJvm(javadocJar = com.vanniktech.maven.publish.JavadocJar.None()))
}
