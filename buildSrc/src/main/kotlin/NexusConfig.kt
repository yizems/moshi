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

object NexusConfig {

    private const val PROJECT_ABS_PATH = "E:/Projects/moshi"

    private val config by lazy {
        Properties()
            .apply {
                try {
                    load(File("$PROJECT_ABS_PATH/nexus.properties").inputStream())
                } catch (e: Exception) {
                    println("$PROJECT_ABS_PATH 目录下未找到 nexus.properties ,无法上传 nexus packages")
                }
            }
    }

    val url by lazy {
        config["URL"]?.toString()
    }
    val userName by lazy {
        config["USER_NAME"]?.toString()
    }
    val pwd by lazy {
        config["PWD"]?.toString()
    }
}
