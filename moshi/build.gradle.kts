/*
 * Copyright (C) 2020 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish")
  id("ru.vyarus.animalsniffer")
}

tasks.withType<KotlinCompile>()
  .matching { it.name.contains("test", true) }
  .configureEach {
    kotlinOptions {
      @Suppress("SuspiciousCollectionReassignment") // It's not suspicious
      freeCompilerArgs += listOf("-Xopt-in=kotlin.ExperimentalStdlibApi")
    }
  }

dependencies {
  compileOnly(Dependencies.jsr305)
  api(Dependencies.okio)

  testCompileOnly(Dependencies.jsr305)
  testImplementation(Dependencies.Testing.junit)
  testImplementation(Dependencies.Testing.truth)
}


publishing {
  publications {
    repositories {
      NexusConfig.nexusUrl?.let {
        maven {
          name = "Nexus"
          url = uri(it)
          isAllowInsecureProtocol = true
          credentials {
            username = NexusConfig.nexusUserName
            password = NexusConfig.nexusPWD
          }
        }
      }

      GithubPackagesConfig.Url?.let {
        maven {
          name = "GithubPackages"
          url = uri(it)
//        isAllowInsecureProtocol = true
          credentials {
            username = GithubPackagesConfig.UserName
            password = GithubPackagesConfig.PWD
          }
        }
      }
    }
  }
}