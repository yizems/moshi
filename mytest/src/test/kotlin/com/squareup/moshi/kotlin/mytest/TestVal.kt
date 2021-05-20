package com.squareup.moshi.kotlin.mytest

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ext.setToDefault
import com.squareup.moshi.ext.toAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

@JsonClass(generateAdapter = true)
data class TestValBean(
    val name: String = "11"
) {
    val app get() = "tttt"
}

class TestVal {
    @Test
    fun testValToJson() {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .setToDefault()

        val ret = TestValBean::class.java.toAdapter()
            .toJson(TestValBean())

        assert(ret.contains("app") && ret.contains("name"))
    }

    @Test
    fun testValFromJson() {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .setToDefault()

        val ret = TestValBean::class.java.toAdapter()
            .fromJson(
                """{
  "name": "22",
  "app": "666"
}
                """.trimIndent()

            )

        assert(ret!!.app == "ttt" && ret.name=="22")
    }
}


