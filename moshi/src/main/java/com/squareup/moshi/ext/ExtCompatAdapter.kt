package com.squareup.moshi.ext

import com.squareup.moshi.FromJson
import java.util.LinkedHashSet

/**
 * 兼容API,
 * 主要用于数据类型不对的处理
 */

internal val extCompatAdapters: List<Any> by lazy {
    listOf(
        BooleanCompatAdapter(),
        ListCompatAdapter(),
        SetCompatAdapter(),
        MapCompatAdapter(),
    )
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

internal class ListCompatAdapter {
    @FromJson
    fun <T> arrayListFromJson(from: List<T>?): ArrayList<T>? {
        from ?: return null
        if (from is ArrayList) {
            return from
        }
        val ret = ArrayList<T>()

        ret.addAll(from)

        return ret
    }

    @FromJson
    fun <T> mutableListFromJsonFor(from: List<T>?): MutableList<T>? {
        return from?.toMutableList()
    }
}

internal class SetCompatAdapter {
    @FromJson
    fun <T> hashSetFromJson(from: Set<T>?): HashSet<T>? {
        from ?: return null
        if (from is HashSet) {
            return from
        }
        val ret = HashSet<T>()

        ret.addAll(from)

        return ret
    }

    @FromJson
    fun <T> mutableSetFromJson(from: Set<T>?): MutableSet<T>? {
        from ?: return null
        if (from is MutableSet) {
            return from
        }
        val ret = mutableSetOf<T>()

        ret.addAll(from)

        return ret
    }

}

internal class MapCompatAdapter {
    @FromJson
    fun <K, V> hashMapFromJson(from: Map<K, V>?): HashMap<K, V>? {
        from ?: return null
        if (from is HashMap) {
            return from
        }
        val ret = HashMap<K, V>()
        ret.putAll(from)
        return ret
    }

    @FromJson
    fun <K, V> linkedHashMapFromJson(from: Map<K, V>?): LinkedHashMap<K, V>? {
        from ?: return null
        if (from is LinkedHashMap) {
            return from
        }
        val ret = LinkedHashMap<K, V>()
        ret.putAll(from)
        return ret
    }

    @FromJson
    fun <K, V> mutableMapFromJson(from: Map<K, V>?): MutableMap<K, V>? {
        from ?: return null
        if (from is MutableMap) {
            return from
        }
        val ret = mutableMapOf<K, V>()
        ret.putAll(from)
        return ret
    }
}
