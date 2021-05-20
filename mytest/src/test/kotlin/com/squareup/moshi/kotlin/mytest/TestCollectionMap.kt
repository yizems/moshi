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
import org.intellij.lang.annotations.Language
import org.junit.Test

@JsonClass(generateAdapter = true)
data class TestCollectionBean(
  val hashmap: MutableMap<String, Any?>? = null,
  var list: MutableList<String>? = null,
  var hashset: MutableSet<String>? = null,
)

class TestCollectionMap {
  @Test
  fun testToJson() {
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
      .setToDefault()

    val ret = TestCollectionBean::class.java.toAdapter()
      .toJson(
        TestCollectionBean(
          hashMapOf(
            "map" to "map"
          ),
          arrayListOf("list1", "list1", "list1"),
          hashSetOf("hs1")
        )
      )
    println(ret)
    assert(
      ret.contains("map")
        && ret.contains("list1")
        && ret.contains("hs1")
    )
  }

  @Test
  fun testValFromJson() {
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
//      .addCollectionExtAdapter()
      .build()
      .setToDefault()

    @Language("JSON")
    val ret = TestCollectionBean::class.java.toAdapter()
      .fromJson(
        """
{
  "list": [
    "list1",
    "list2"
  ],
  "hashmap": {
    "map1": "map1",
    "map2": "map2"
  },
  "hashset": [
    "hs1",
    "hs2"
  ]
}
                """.trimIndent()

      )
    val toStr=ret.toString()
    println(toStr)
    assert(
      toStr.contains("list1")
        && toStr.contains("map1")
        && toStr.contains("hs1")
    )
  }
}


