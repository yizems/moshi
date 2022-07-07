import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

buildscript {
  dependencies {
    val kotlinVersion = System.getenv("MOSHI_KOTLIN_VERSION")
      ?: libs.versions.kotlin.get()
    val kspVersion = System.getenv("MOSHI_KSP_VERSION")
      ?: libs.versions.ksp.get()
    classpath(kotlin("gradle-plugin", version = kotlinVersion))
    classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:$kspVersion")
    // https://github.com/melix/japicmp-gradle-plugin/issues/36
    classpath("com.google.guava:guava:28.2-jre")
  }
}

plugins {
  alias(libs.plugins.mavenPublish) apply false
}

val VERSION_NAME: String by project

allprojects {
  group = "com.squareup.moshi"
  version = VERSION_NAME
  println(VERSION_NAME)

  repositories {
    maven {
      isAllowInsecureProtocol = true
      setUrl("http://maven.aliyun.com/nexus/content/groups/public/")
    }
    mavenCentral()
  }
}


subprojects {
  // Apply with "java" instead of just "java-library" so kotlin projects get it too
  pluginManager.withPlugin("java") {
    configure<JavaPluginExtension> {
      if (project.name == "moshi-kotlin-codegen") {
        toolchain {
          languageVersion.set(JavaLanguageVersion.of(11))
        }
      } else {
        toolchain {
          languageVersion.set(JavaLanguageVersion.of(8))
        }
      }
    }
  }

  pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    tasks.withType<KotlinCompile>().configureEach {
      kotlinOptions {
        // TODO re-enable when no longer supporting multiple kotlin versions
//        @Suppress("SuspiciousCollectionReassignment")
//        freeCompilerArgs += listOf("-progressive")
        jvmTarget = libs.versions.jvmTarget.get()
      }
    }

    configure<KotlinProjectExtension> {
      if (project.name != "examples") {
        explicitApi()
      }
    }
  }
}

allprojects {

  repositories {
    maven {
      isAllowInsecureProtocol = true
      setUrl("http://maven.aliyun.com/nexus/content/groups/public/")
    }
    MavenConfig.getRepositories()
      .forEach {
        maven {
          url = uri(it.url)
          credentials {
            username = it.userName
            password = it.pwd
          }
        }

        if (!it.urlSnapshot.isNullOrBlank()) {
          maven {
            url = uri(it.urlSnapshot!!)
            credentials {
              username = it.userName
              password = it.pwd
            }
          }
        }
      }
  }



  plugins.withId("com.vanniktech.maven.publish.base") {

    extensions.configure(PublishingExtension::class.java) {
      publications {
        repositories {
          MavenConfig.getRepositories().forEach { mavenConfig ->
            maven {
              name = mavenConfig.name

              val versionName = project.findProperty("VERSION_NAME")
                ?.toString()

              println(versionName)

              val snapshot = versionName?.endsWith("SNAPSHOT") ?: false

              if (snapshot && mavenConfig.urlSnapshot.isNullOrBlank()) {
                throw java.lang.IllegalArgumentException("snapshot 地址没有配置")
              }

              url = uri(if (!snapshot) mavenConfig.url else mavenConfig.urlSnapshot!!)
              isAllowInsecureProtocol = true
              credentials {
                username = mavenConfig.userName
                password = mavenConfig.pwd
              }
            }
          }
        }
      }
    }

  }
}
