package com.squareup.moshi.ext

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 * 默认模块和属性, 用于内部或其他衍生库
 */

private var _moshInstances: Moshi = Moshi
  .Builder()
  .build()

public val moshiInstances: Moshi
  get() = _moshInstances

public fun Moshi.setToDefault() {
  _moshInstances = this
}


//--------------------扩展函数------------------------

/**
 * to JsonAdapter
 * @param type 泛型
 */
public fun <T> Class<T>.fromJson(str: String): T? {
  return _moshInstances.adapter(this).fromJson(str)
}

public fun <T> Type.fromJson(str: String): T? {
  return _moshInstances.adapter<T>(this).fromJson(str)
}

/**
 * to type: 添加泛型参数
 * @param type 泛型
 */
public fun Type.newParameterizedType(vararg type: Type): Type {
  if (type.isNotEmpty()) {
    return Types.newParameterizedType(this, *type)
  }
  return this
}

public fun <T> Type.toAdapter(vararg type: Type): JsonAdapter<T> {
  if (type.isNotEmpty()) {
    return _moshInstances.adapter(Types.newParameterizedType(this, *type))
  }
  return _moshInstances.adapter(this)
}

public fun <T> Class<T>.toAdapter(): JsonAdapter<T> {
  return _moshInstances.adapter(this)
}

/**
 * 转换为字符串
 */
public inline fun <reified T> T.toJsonString(): String {
  return T::class.java.toAdapter()
    .toJson(this)
}

/**
 * 转换为别的对象
 */
public inline fun <reified T, R> T.toOtherBean(clz: Class<R>): R? {
  return clz
    .toAdapter()
    .fromJson(
      T::class.java
        .toAdapter()
        .toJson(this)
    )
}

/**
 * 转换为别的对象
 */
public inline fun <reified T, R> T.toOtherBean(type: Type): R? {
  return type
    .toAdapter<R>()
    .fromJson(
      T::class.java
        .toAdapter()
        .toJson(this)
    )
}