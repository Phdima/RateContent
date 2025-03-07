package com.example.ratecontent.data.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

data class BookResponse(
    @Json(name = "totalItems") val totalItems: Int,
    @Json(name = "items") val items: List<BookItem>?
)

data class BookItem(
    @Json(name = "id") val id: String,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @Json(name = "title") val title: String,
    @Json(name = "authors") val authors: List<String>?,
    @Json(name = "description") val description: String?,
    @Json(name = "averageRating") val averageRating: Double?,
    @Json(name = "imageLinks") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @Json(name = "thumbnail") val thumbnail: String?,
    @Json(name = "smallThumbnail") val smallThumbnail: String?
)

interface BookApiService {
    @GET("volumes")
   suspend fun searchBooks(
        @Query("q") query: String,
        @Query("langRestrict") language: String = "ru",
        @Query("maxResults") maxResults: Int = 20
    ): BookResponse
}
