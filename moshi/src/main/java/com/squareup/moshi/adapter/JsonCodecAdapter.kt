package com.squareup.moshi.adapter

import com.squareup.moshi.*
import com.squareup.moshi.JsonAdapter.Factory
import java.lang.reflect.Type

/**
 * 用来处理 字段是 json字符串的情况, 可以自动解析json字符串到类,或者类到json字符串
 * @property fromJson Boolean
 * @property toJson Boolean
 * @constructor
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
public annotation class JsonCodec(
        val fromJson: Boolean = true,
        val toJson: Boolean = true,
)


public class JsonCodecAdapter(public val type: Type, public val moshi: Moshi, public val fromJson: Boolean, public val toJson: Boolean) : JsonAdapter<Any?>() {

    public companion object {
        public val FACTORY: Factory by lazy {
            Factory { type, annotations, moshi ->
                val jsonStringAnnotation = annotations
                        .firstOrNull { JsonCodec::class.isInstance(it) }
                        as? JsonCodec
                        ?: return@Factory null

                return@Factory JsonCodecAdapter(
                        type,
                        moshi,
                        jsonStringAnnotation.fromJson,
                        jsonStringAnnotation.toJson,
                )
            }
        }
    }

    private val typeAdapter by lazy {
        moshi.adapter<Any?>(type)
    }

    override fun fromJson(reader: JsonReader): Any? {
        val str = reader.nextString()
        return if (fromJson) {
            typeAdapter.fromJson(str)
        } else {
            typeAdapter.fromJson(reader)
        }
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        if (toJson) {
            writer.value(typeAdapter.toJson(value))
        } else {
            typeAdapter.toJson(writer, value)
        }
    }
}
