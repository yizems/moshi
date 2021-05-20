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
package com.squareup.moshi.kotlin.mytest

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ext.setToDefault
import com.squareup.moshi.ext.toAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

@JsonClass(generateAdapter = true)
data class TestValBean(
    val name: String = "11"
) {
    val app get() = "tttt"
}

class TestVal {
    @Test
    fun testValToJson() {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .setToDefault()

        val ret = TestValBean::class.java.toAdapter()
            .toJson(TestValBean())

        assert(ret.contains("app") && ret.contains("name"))
    }

    @Test
    fun testValFromJson() {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .setToDefault()

        val ret = TestValBean::class.java.toAdapter()
            .fromJson(
                """{
  "name": "22",
  "app": "666"
}
                """.trimIndent()

            )

        assert(ret!!.app == "ttt" && ret.name=="22")
    }
}


