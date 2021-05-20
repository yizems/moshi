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
import com.squareup.moshi.kotlin.jobj.TypeUtils.castToString
import com.squareup.moshi.kotlin.jobj.TypeUtils.wrapValue
import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger


public class MJsonArray : Serializable {

  private var innerList: MutableList<Any?>

  public constructor() {
    innerList = ArrayList(10)
  }

  public constructor(list: List<Any?>?) {
    this.innerList = list?.toMutableList() ?: ArrayList(10)

    if (list == null) {
      this.innerList = ArrayList(10)
      return
    }

    this.innerList = ArrayList(list.size)

    list.forEach {
      innerList.add(wrapValue(it))
    }
  }

  public constructor(initialCapacity: Int) {
    innerList = ArrayList(initialCapacity)
  }

  public fun size(): Int {
    return innerList.size
  }

  public fun isEmpty(): Boolean = innerList.isEmpty()

  public operator fun contains(o: Any): Boolean {
    return innerList.contains(o)
  }

  public operator fun iterator(): Iterator<Any?> {
    return innerList.iterator()
  }

  public fun add(e: Any?): Boolean {
    return innerList.add(wrapValue(e))
  }

  public fun remove(o: Any?): Boolean {
    return innerList.remove(o)
  }

  public fun containsAll(c: Collection<*>): Boolean {
    return innerList.containsAll(c)
  }

  public fun addAll(c: Collection<Any?>): Boolean {
    c.forEach {
      innerList.add(wrapValue(it))
    }
    return true
  }

  public fun removeAll(c: Collection<*>): Boolean {
    return innerList.removeAll(c)
  }

  public fun retainAll(c: Collection<*>): Boolean {
    return innerList.retainAll(c)
  }

  public fun clear() {
    innerList.clear()
  }

  public operator fun set(index: Int, element: Any?): Any? {
    return innerList.set(index, wrapValue(element))
  }

  public fun add(index: Int, element: Any?) {
    innerList.add(index, wrapValue(element))
  }

  public fun remove(index: Int): Any? {
    return innerList.removeAt(index)
  }

  public fun indexOf(o: Any?): Int {
    return innerList.indexOf(o)
  }

  public fun lastIndexOf(o: Any?): Int {
    return innerList.lastIndexOf(o)
  }

  public fun listIterator(): ListIterator<Any?> {
    return innerList.listIterator()
  }

  public fun listIterator(index: Int): ListIterator<Any?> {
    return innerList.listIterator(index)
  }

  public fun subList(fromIndex: Int, toIndex: Int): List<Any?> {
    return innerList.subList(fromIndex, toIndex)
  }

  public operator fun get(index: Int): Any? {
    return innerList[index]
  }


  public fun getBoolean(index: Int): Boolean? {
    val value = get(index) ?: return null
    return castToBoolean(value)
  }

  public fun getBooleanValue(index: Int): Boolean {
    return getBoolean(index) ?: return false
  }

  public fun getInteger(index: Int): Int? {
    val value = get(index)
    return castToInt(value)
  }

  public fun getIntValue(index: Int): Int {
    return getInteger(index) ?: 0
  }

  public fun getLong(index: Int): Long? {
    val value = get(index)
    return castToLong(value)
  }

  public fun getLongValue(index: Int): Long {
    return getLong(index) ?: 0L
  }

  public fun getFloat(index: Int): Float? {
    val value = get(index)
    return castToFloat(value)
  }

  public fun getFloatValue(index: Int): Float {
    return getFloat(index) ?: 0F
  }

  public fun getDouble(index: Int): Double? {
    val value = get(index)
    return castToDouble(value)
  }

  public fun getDoubleValue(index: Int): Double {
    return getDouble(index) ?: 0.0
  }

  public fun getBigDecimal(index: Int): BigDecimal? {
    val value = get(index)
    return castToBigDecimal(value)
  }

  public fun getBigInteger(index: Int): BigInteger? {
    val value = get(index)
    return castToBigInteger(value)
  }

  public fun getString(index: Int): String? {
    val value = get(index)
    return castToString(value)
  }

  public fun clone(): Any {
    return MJsonArray(innerList)
  }

  override fun equals(other: Any?): Boolean {
    return innerList == other
  }

  override fun hashCode(): Int {
    return innerList.hashCode()
  }

  public fun getMJsonObject(index: Int): MJsonObject? {
    return innerList[index] as? MJsonObject
  }

  public fun getMJsonArray(index: Int): MJsonArray? {
    return innerList[index] as? MJsonArray
  }

  public fun getInnerList(): List<Any?> = innerList
}