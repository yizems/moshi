package com.squareup.moshi.ext

import com.squareup.moshi.FromJson

/**
 * 兼容API,
 * 主要用于数据类型不对的处理
 */

internal val extCompatAdapters: List<Any> by lazy {
    listOf(
        BooleanCompatAdapter(),
    )
}

internal class BooleanCompatAdapter {
    @FromJson
    public fun fromJson(from: String?): Boolean {
        return from?.equals("1", true) == true
                || from?.equals("true", true) == true
    }

    @FromJson
    public fun fromJson(from: Int): Boolean {
        return from == 1
    }
}
