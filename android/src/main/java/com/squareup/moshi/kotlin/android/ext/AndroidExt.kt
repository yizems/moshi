package com.squareup.moshi.kotlin.android.ext

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.Serializable

/**
 * 支持 [MJsonObject] 和 [MJsonArray] ,并且实现了 [Serializable] 接口
 */
public fun Moshi.Builder.addAndroidJsonSupport(): Moshi.Builder {
    return this.add(AndroidJsonAdapter())
}

public class AndroidJsonAdapter {

    @FromJson
    public fun objFromJson(map: Map<Any?, Any?>?): MJsonObject? {
        return MJsonObject(map?.toMutableMap() ?: return null)
    }

    @ToJson
    public fun objToJson(obj: MJsonObject?): String? {
        return obj?.toString()
    }

    @FromJson
    public fun arrayFromJson(str: List<Any?>?): MJsonArray? {
        return MJsonArray(str?.toMutableList() ?: return null)
    }

    @ToJson
    public fun arrayToJson(obj: MJsonArray?): String? {
        return obj?.toString()
    }
}


public class MJsonObject : JSONObject, Serializable {

    public constructor() : super()
    public constructor(copyFrom: MutableMap<Any?, Any?>?) : super(copyFrom)
    public constructor(readFrom: JSONTokener?) : super(readFrom)
    public constructor(json: String?) : super(json)
    public constructor(copyFrom: JSONObject?, names: Array<out String>?) : super(copyFrom, names)
}

public class MJsonArray : JSONArray, Serializable {
    public constructor() : super()
    public constructor(copyFrom: MutableCollection<Any?>?) : super(copyFrom)
    public constructor(readFrom: JSONTokener?) : super(readFrom)
    public constructor(json: String?) : super(json)
    public constructor(array: Any?) : super(array)
}
