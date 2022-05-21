package com.squareup.moshi.adapter

import com.squareup.moshi.*
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8
import java.lang.reflect.Type

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
public annotation class Base64Qualifier(
    val encode: Boolean = true,
    val decode: Boolean = true,
)

public class Base64QualifierAdapter(
    private val encode: Boolean,
    private val decode: Boolean,
) : JsonAdapter<String>() {

    public class Factory : JsonAdapter.Factory {

        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): Base64QualifierAdapter? {
            if (!String::class.java.isAssignableFrom(type.rawType)) {
                return null
            }
            val annotation = annotations.find { it is Base64Qualifier } as? Base64Qualifier ?: return null
            return Base64QualifierAdapter(annotation.encode, annotation.decode)
        }
    }

    override fun fromJson(reader: JsonReader): String? {

        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        val source = reader.nextString()

        if (source.isBlank()) {
            return source
        }

        return if (decode) {
            val base64 = source.decodeBase64() ?: throw IllegalArgumentException("Base64 decode error: $source")
            base64.utf8()
        } else {
            source
        }
    }

    override fun toJson(writer: JsonWriter, value: String?) {
        if (value.isNullOrBlank()) {
            writer.nullValue()
            return
        }
        val ret = if (encode) {
            value.encodeUtf8().base64()
        } else {
            value
        }
        writer.value(ret)
    }

}
