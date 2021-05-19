package com.squareup.moshi.kotlin.jobj

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson


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
    public fun objToJson(obj:MJsonObject?): Map<String, Any?>? {
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