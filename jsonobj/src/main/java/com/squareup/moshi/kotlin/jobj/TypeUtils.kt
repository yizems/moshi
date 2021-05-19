/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 简化自 FastJson
 */
package com.squareup.moshi.kotlin.jobj

import com.squareup.moshi.JsonDataException
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public object TypeUtils {

    public fun castToString(value: Any?): String? {
        return value?.toString()
    }

    public fun castToBigDecimal(value: Any?): BigDecimal? {
        if (value == null) {
            return null
        }
        if (value is BigDecimal) {
            return value
        }
        if (value is BigInteger) {
            return BigDecimal(value as BigInteger?)
        }
        val strVal = value.toString()
        return if (strVal.length == 0 //
            || "null" == strVal
        ) {
            null
        } else BigDecimal(strVal)
    }

    public fun castToBigInteger(value: Any?): BigInteger? {
        if (value == null) {
            return null
        }
        if (value is BigInteger) {
            return value
        }
        if (value is Float || value is Double) {
            return BigInteger.valueOf((value as Number).toLong())
        }
        val strVal = value.toString()
        return if (strVal.length == 0 //
            || "null" == strVal
        ) {
            null
        } else BigInteger(strVal)
    }

    public fun castToFloat(value: Any?): Float? {
        if (value == null) {
            return null
        }
        if (value is Number) {
            return value.toFloat()
        }
        if (value is String) {
            val strVal = value.toString()
            return if (strVal.length == 0 //
                || "null" == strVal
            ) {
                null
            } else strVal.toFloat()
        }
        throw JsonDataException("can not cast to float, value : $value")
    }

    public fun castToDouble(value: Any?): Double? {
        if (value == null) {
            return null
        }
        if (value is Number) {
            return value.toDouble()
        }
        if (value is String) {
            val strVal = value.toString()
            return if (strVal.length == 0 //
                || "null" == strVal || "NULL" == strVal
            ) {
                null
            } else strVal.toDouble()
        }
        throw JsonDataException("can not cast to double, value : $value")
    }

    public fun castToLong(value: Any?): Long? {
        if (value == null) {
            return null
        }
        if (value is Number) {
            return value.toLong()
        }
        if (value is String) {
            val strVal = value
            if (strVal.length == 0 //
                || "null" == strVal
            ) {
                return null
            }
            try {
                return strVal.toLong()
            } catch (ex: NumberFormatException) {
                //
            }
        }
        throw JsonDataException("can not cast to long, value : $value")
    }

    public fun castToInt(value: Any?): Int? {
        if (value == null) {
            return null
        }
        if (value is Int) {
            return value
        }
        if (value is Number) {
            return value.toInt()
        }
        if (value is String) {
            val strVal = value
            return if (strVal.length == 0 //
                || "null" == strVal
            ) {
                null
            } else strVal.toInt()
        }
        throw JsonDataException("can not cast to int, value : $value")
    }

    public fun castToBoolean(value: Any?): Boolean? {
        if (value == null) {
            return null
        }
        if (value is Boolean) {
            return value
        }
        if (value is Number) {
            return value.toInt() == 1
        }
        if (value is String) {
            val strVal = value
            if (strVal.length == 0 //
                || "null" == strVal
            ) {
                return null
            }
            if ("true".equals(strVal, ignoreCase = true) //
                || "1" == strVal
            ) {
                return java.lang.Boolean.TRUE
            }
            if ("false".equals(strVal, ignoreCase = true) //
                || "0" == strVal
            ) {
                return java.lang.Boolean.FALSE
            }
        }
        throw JsonDataException("can not cast to int, value : $value")
    }

    @Suppress("UNCHECKED_CAST")
    internal fun wrapValue(any: Any?): Any? {
        any ?: return null
        if (any is Map<*, *>) {
            return MJsonObject(any as? Map<String, Any?> ?: return null)
        }
        if (any is List<*>){
            return MJsonArray(any)
        }
        return any
    }
}