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
import com.squareup.moshi.ext.fromJson
import com.squareup.moshi.ext.newParameterizedType
import com.squareup.moshi.ext.setToDefault
import com.squareup.moshi.ext.toAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

@JsonClass(generateAdapter = true)
data class TestDefaultBean(
  var name: String = "11"
) {
  val app get() = "tttt"
}

class TestDefault {
  @Test
  fun testValToJson() {

    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
      .setToDefault()

    val bean = TestDefaultBean::class.java.fromJson(
      """
            {
            "name":"8888"
            }
        """.trimIndent()
    )

    val map = Map::class.java.newParameterizedType(String::class.java, Any::class.java)
      .fromJson<Map<String, Any>>(
        """
                 {
            "name":"8888",
            "list":[1,2,3,4]
            }
            """.trimIndent()
      )

    println(bean)
    println(map)
  }

}


