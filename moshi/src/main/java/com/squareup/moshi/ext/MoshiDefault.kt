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
public fun <T> Class<T>.toAdapter(vararg type: Type): JsonAdapter<T> {
    return _moshInstances.adapter(this.newParameterizedType(*type))
}

/**
 * to type: 添加泛型参数
 * @param type 泛型
 */
public fun <T> Class<T>.newParameterizedType(vararg type: Type): Type {
    if (type.isNotEmpty()) {
        return Types.newParameterizedType(this, *type)
    }
    return this
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