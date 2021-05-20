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

plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish")
}

dependencies {
  api(project(":moshi"))

  testImplementation(kotlin("test"))
  testImplementation(Dependencies.Testing.junit)
  testImplementation(Dependencies.Testing.truth)

  testImplementation("org.apache.commons:commons-lang3:3.12.0")
}

publishing {
  publications {
    repositories {
      NexusConfig.url?.let {
        maven {
          name = "Nexus"
          url = uri(it)
          isAllowInsecureProtocol = true
          credentials {
            username = NexusConfig.userName
            password = NexusConfig.pwd
          }
        }
      }

      GithubPackagesConfig.url?.let {
        maven {
          name = "GithubPackages"
          url = uri(it)
//        isAllowInsecureProtocol = true
          credentials {
            username = GithubPackagesConfig.userName
            password = GithubPackagesConfig.pwd
          }
        }
      }
    }
  }
}