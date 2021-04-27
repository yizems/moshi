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

    @FromJson
    fun <T> linkedHashSetFromJson(from: Set<T>?): LinkedHashSet<T>? {
        from ?: return null
        if (from is LinkedHashSet) {
            return from
        }
        val ret = LinkedHashSet<T>()

        ret.addAll(from)

        return ret
    }
}

internal class MapCompatAdapter {
    @FromJson
    fun <K,V> hashMapFromJson(from: Map<K,V>?): HashMap<K,V>? {
        from ?: return null
        if (from is HashMap) {
            return from
        }
        val ret = HashMap<K,V>()
        ret.putAll(from)
        return ret
    }

    @FromJson
    fun <K,V> linkedHashMapFromJson(from: Map<K,V>?): LinkedHashMap<K,V>? {
        from ?: return null
        if (from is LinkedHashMap) {
            return from
        }
        val ret = LinkedHashMap<K,V>()
        ret.putAll(from)
        return ret
    }
    @FromJson
    fun <K,V> mutableMapFromJson(from: Map<K,V>?): MutableMap<K,V>? {
        from ?: return null
        if (from is MutableMap) {
            return from
        }
        val ret = mutableMapOf<K,V>()
        ret.putAll(from)
        return ret
    }
}
