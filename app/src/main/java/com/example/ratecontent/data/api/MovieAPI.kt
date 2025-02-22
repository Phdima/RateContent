package com.example.ratecontent.data.api

import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class SearchMovieResponse(
    @Json(name = "results") val results: List<Movie>,
)

data class Movie(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
)

interface TMDbApiService {
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("language") language: String = "ru-RU",
    ): SearchMovieResponse
}



