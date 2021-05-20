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

import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBigDecimal
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBigInteger
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToBoolean
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToDouble
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToFloat
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToInt
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToLong
import com.squareup.moshi.kotlin.jobj.TypeUtils.wrapValue
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger

public class MJsonObject : Serializable {

  private var innerMap: MutableMap<String, Any?>

  public constructor(initialCapacity: Int = 16, ordered: Boolean = false) {
    innerMap = if (ordered) {
      LinkedHashMap(initialCapacity)
    } else {
      HashMap(initialCapacity)
    }
  }

  public constructor(map: Map<String, Any?>) {
    this.innerMap = HashMap<String, Any?>(16)

    map.forEach { (t, u) ->
      innerMap[t] = wrapValue(u)
    }


  }

  public fun size(): Int {
    return innerMap.size
  }

  public fun isEmpty(): Boolean {
    return innerMap.isEmpty()
  }

  public fun containsKey(key: Any?): Boolean {
    return innerMap.containsKey(key)
  }

  public fun containsValue(value: Any?): Boolean {
    return innerMap.containsValue(value)
  }

  public operator fun get(key: Any?): Any? {
    return innerMap[key]
  }

  public operator fun set(key: String, value: Any?): Any? {
    return put(key, value)
  }


  public fun getBoolean(key: String): Boolean? {
    val value = get(key) ?: return null
    return castToBoolean(value)
  }

  public fun getBooleanValue(key: String): Boolean {
    val value = getBoolean(key)
    return value ?: false
  }

  public fun getInteger(key: String?): Int? {
    val value = get(key)
    return castToInt(value)
  }

  public fun getIntValue(key: String?): Int {
    val value = get(key)
    return castToInt(value) ?: return 0
  }

  public fun getLong(key: String?): Long? {
    val value = get(key)
    return castToLong(value)
  }

  public fun getLongValue(key: String?): Long {
    val value = get(key)
    return castToLong(value) ?: return 0L
  }

  public fun getFloat(key: String?): Float? {
    val value = get(key)
    return castToFloat(value)
  }

  public fun getFloatValue(key: String?): Float {
    val value = get(key)
    return castToFloat(value) ?: return 0f
  }

  public fun getDouble(key: String?): Double? {
    val value = get(key)
    return castToDouble(value)
  }

  public fun getDoubleValue(key: String?): Double {
    val value = get(key)
    return castToDouble(value) ?: return 0.0
  }

  public fun getBigDecimal(key: String?): BigDecimal? {
    val value = get(key)
    return castToBigDecimal(value)
  }

  public fun getBigInteger(key: String?): BigInteger? {
    val value = get(key)
    return castToBigInteger(value)
  }

  public fun getString(key: String?): String? {
    val value = get(key) ?: return null
    return value.toString()
  }

  public fun put(key: String, value: Any?): Any? {
    return innerMap.put(key, wrapValue(value))
  }

  public fun putAll(m: Map<out String, Any?>) {
    m.forEach { (s, u) ->
      innerMap[s] = wrapValue(u)
    }
  }

  public fun clear() {
    innerMap.clear()
  }

  public fun remove(key: Any?): Any? {
    return innerMap.remove(key)
  }

  public fun keySet(): Set<String> {
    return innerMap.keys
  }

  public fun values(): Collection<Any?> {
    return innerMap.values
  }

  public fun entrySet(): Set<Map.Entry<String, Any?>?> {
    return innerMap.entries
  }

  public fun clone(): Any {
    return MJsonObject(LinkedHashMap(innerMap))
  }

  public override fun equals(other: Any?): Boolean {
    return innerMap == other
  }

  override fun hashCode(): Int {
    return innerMap.hashCode()
  }

  public fun getInnerMap(): Map<String, Any?> {
    return innerMap
  }

  public fun getMJsonObject(key: String): MJsonObject? {
    return innerMap[key] as? MJsonObject
  }

  public fun getMJsonArray(key: String): MJsonArray? {
    return innerMap[key] as? MJsonArray
  }
}