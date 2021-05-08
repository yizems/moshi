package com.squareup.moshi.kotlin.android.ext

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.Serializable


public fun Moshi.Builder.addAndroidJsonSupport(): Moshi.Builder {
    return this.add(AndroidJsonAdapter())
}


public class AndroidJsonAdapter() {

    @FromJson
    public fun objFromJson(str: String?): MJsonObject? {
        return MJsonObject(str ?: return null)
    }

    @ToJson
    public fun objToJson(obj: MJsonObject?): String? {
        return obj?.toString()
    }

    @FromJson
    public fun arrayFromJson(str: String?): MJsonArray? {
        return MJsonArray(str ?: return null)
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
