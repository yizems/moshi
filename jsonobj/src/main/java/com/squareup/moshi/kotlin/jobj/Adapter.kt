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
package com.squareup.moshi.kotlin.jobj

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.ext.fromJson


public fun Moshi.Builder.addMJsonAdapter(): Moshi.Builder {
  this.add(MJsonAdapter())
  return this
}

public class MJsonAdapter() {

  @FromJson
  public fun objFromJson(map: Map<String, Any?>?): MJsonObject? {
    return MJsonObject(map ?: return null)
  }

  @ToJson
  public fun objToJson(obj: MJsonObject?): Map<String, Any?>? {
    obj ?: return null
    return obj.getInnerMap()
  }

  @FromJson
  public fun arrayFromJson(list: List<Any?>?): MJsonArray? {
    return MJsonArray(list ?: return null)
  }

  @ToJson
  public fun arrayToJson(array: MJsonArray?): List<Any?>? {
    return array?.getInnerList()
  }
}

public fun String.toMJsonObject(): MJsonObject? {
  return MJsonObject::class.java
    .fromJson(this)
}

public fun String.toMJsonArray(): MJsonArray? {
  return MJsonArray::class.java
    .fromJson(this)
}