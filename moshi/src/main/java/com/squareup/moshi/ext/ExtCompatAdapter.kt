package com.squareup.moshi.ext

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi

/**
 * Boolean 值兼容
 *
 * 具体查看 [BooleanCompatAdapter.fromString]
 */
public fun Moshi.Builder.addBooleanCompatAdapter(): Moshi.Builder {
    this.add(BooleanCompatAdapter())
    return this
}

public open class BooleanCompatAdapter {
    @FromJson
    public open fun fromString(from: Any?): Boolean {

        from ?: return false

        return when (from) {
            is Boolean -> return from
            is String -> return from == "1" || from.equals("true", true)
            is Number -> from.toInt() == 1
            else -> false
        }
    }
}

