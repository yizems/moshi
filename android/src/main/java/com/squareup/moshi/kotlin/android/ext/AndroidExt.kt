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

package com.squareup.moshi.kotlin.android.ext

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * 支持 [MJsonObject] 和 [MJsonArray] ,并且实现了 [Serializable] 接口
 */
public fun Moshi.Builder.addAndroidJsonSupport(): Moshi.Builder {
    return this.add(AndroidJsonAdapter())
}

public class AndroidJsonAdapter {

    @FromJson
    public fun objFromJson(map: Map<Any?, Any?>?): JSONObject? {
        return JSONObject(map?.toMutableMap() ?: return null)
    }

    @ToJson
    public fun objToJson(obj: JSONObject?): String? {
        return obj?.toString()
    }

    @FromJson
    public fun arrayFromJson(str: List<Any?>?): JSONArray? {
        return JSONArray(str?.toMutableList() ?: return null)
    }

    @ToJson
    public fun arrayToJson(obj: JSONArray?): String? {
        return obj?.toString()
    }
}
