package com.squareup.moshi.kotlin.codegen.jsonignore

import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonIgnore
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.junit.Test

class JsonIgnoreTest {

    @JsonClass(generateAdapter = true)
    data class TestSerialize(
        @JsonIgnore
        var abced1: String = "",
        @JsonIgnore(serialize = false)
        var abced2: String = "",
        var abced3: String = ""
    ) {
        @JsonIgnore
        var ccc1 = ""

        @JsonIgnore(deserialize = true)
        var ccc2 = ""

        var ccc3 = ""
    }

    @Test
    fun test_is_present() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<TestSerialize>()
        val json = adapter.toJson(
            TestSerialize("1", "2", "3")
        )
        println(json)

        adapter.fromJson("{\"abced3\":\"3\",\"ccc1\":\"\"}")
//        Truth.assertThat(json).contains("ccc3")
    }

}