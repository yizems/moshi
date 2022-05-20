package com.squareup.moshi.adapter

import com.squareup.moshi.*
import com.squareup.moshi.JsonAdapter.Factory

/**
 * 枚举类序列化适配器, 枚举类只需要根据序列化类型, 继承 [EnumJsonInt] [EnumJsonString] 即可完成序列化和反序列化
 */

/**
 * 适用于 json类型为 int 枚举序列化指示器
 */
public interface EnumJsonInt {
    /** 最好为String,Int*/
    public fun getJsonValue(): Int?
}

/**
 * 适用于 json类型为 String 枚举序列化指示器
 */
public interface EnumJsonString {
    /** 最好为String,Int*/
    public fun getJsonValue(): String?
}

public class EnumJsonTypeAdapter(
        public val type: Class<*>,
        public val moshi: Moshi,
) : JsonAdapter<Any?>() {

    public companion object {
        public val FACTORY: Factory by lazy {
            Factory { type, _, moshi ->
                val rawType = Types.getRawType(type)
                if (rawType.isEnum
                        && (EnumJsonInt::class.java.isAssignableFrom(rawType)
                                || EnumJsonString::class.java.isAssignableFrom(rawType))
                ) {
                    return@Factory EnumJsonTypeAdapter(
                            rawType,
                            moshi,
                    )
                } else {
                    return@Factory null
                }
            }
        }
    }

    private val isInt = EnumJsonInt::class.java.isAssignableFrom(type)

    override fun fromJson(reader: JsonReader): Any? {

        if (reader.peek() == JsonReader.Token.NULL) {
            reader.skipValue()
            return null
        }

        val value = if (isInt) reader.nextInt() else reader.nextString()

        val values = type.enumConstants

        return if (isInt) {
            values.filterIsInstance<EnumJsonInt>()
                    .firstOrNull {
                        it.getJsonValue() == value
                    }
        } else {
            values.filterIsInstance<EnumJsonString>()
                    .firstOrNull {
                        it.getJsonValue() == value
                    }
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        if (isInt) {
            writer.value((value as? EnumJsonInt)?.getJsonValue())
        } else {
            writer.value((value as? EnumJsonString)?.getJsonValue())
        }
    }
}
