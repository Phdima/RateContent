package com.example.ratecontent.data.local.utility

import androidx.room.TypeConverter
import com.example.ratecontent.data.api.ImageLinks
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonAdapter = moshi.adapter(ImageLinks::class.java)

    @TypeConverter
    fun fromImageLinks(imageLinks: ImageLinks?): String {
        return imageLinks?.let { jsonAdapter.toJson(it) } ?: ""
    }

    @TypeConverter
    fun toImageLinks(json: String): ImageLinks? {
        return if (json.isEmpty()) null else jsonAdapter.fromJson(json)
    }
}