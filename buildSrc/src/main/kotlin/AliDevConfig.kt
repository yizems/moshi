/**
 * Copyright (c) 2021 yizems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File
import java.util.*

object AliDevConfig {

    private val PROJECT_ABS_PATH = System.getProperty("user.home") + "/.m2"

    private val config by lazy {
        Properties()
            .apply {
                try {
                    load(File("$PROJECT_ABS_PATH/alidev.properties").inputStream())
                } catch (e: Exception) {
                    println("$PROJECT_ABS_PATH 目录下未找到 github.properties ,无法上传 github packages")
                }
            }
    }

    val url by lazy {
        config["MAVEN_PATH"]?.toString()
    }
    val userName by lazy {
        config["MAVEN_USER"]?.toString()
    }
    val pwd by lazy {
        config["MAVEN_PWD"]?.toString()
    }
}
