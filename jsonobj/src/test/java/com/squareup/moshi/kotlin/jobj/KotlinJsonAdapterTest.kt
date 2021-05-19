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
package com.squareup.moshi.kotlin.jobj

import com.squareup.moshi.Moshi
import com.squareup.moshi.ext.setToDefault
import com.squareup.moshi.ext.toAdapter
import org.apache.commons.lang3.SerializationUtils
import org.junit.Test


class KotlinJsonAdapterTest {
    @Test
    fun testJsonObj() {

        Moshi.Builder().addMJsonAdapter()
            .build()
            .setToDefault()

        val str = """
{
  "abc": [
    1,
    2,
    "3"
  ],
  "ccc": {
    "tt": "tt",
    "bb": "22"
  }
}
        """.trimIndent()

        val ret = MJsonObject::class.java.toAdapter()
            .fromJson(str)

        println(ret)

        println(MJsonObject::class.java.toAdapter().toJson(ret))
    }

    @Test
    fun testJsonArray() {

        Moshi.Builder().addMJsonAdapter()
            .build()
            .setToDefault()

        val str = """
[
  1,
  2,
  3,
  {
    "tt": "tt",
    "bb": "bb",
    "cc": [
      33,
      32,
      32
    ]
  }
]
        """.trimIndent()

        val ret = MJsonArray::class.java.toAdapter()
            .fromJson(str)

        println(ret)

        println(MJsonArray::class.java.toAdapter().toJson(ret))
    }


    @Test
    fun testSerialize(){
        Moshi.Builder().addMJsonAdapter()
            .build()
            .setToDefault()

        val str = """
{
  "abc": [
    1,
    2,
    "3"
  ],
  "ccc": {
    "tt": "tt",
    "bb": "22"
  }
}
        """.trimIndent()

        val ret = MJsonObject::class.java.toAdapter()
            .fromJson(str)

        val clone = SerializationUtils.clone(ret)
        println(clone == ret)
        println(clone)
    }
}
