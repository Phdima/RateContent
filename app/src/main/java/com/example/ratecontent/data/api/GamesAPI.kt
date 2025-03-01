package com.example.ratecontent.data.api

import com.example.ratecontent.utils.GamesAPIConstants
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

data class GameSearchResponse(
  @Json(name = "results") val results: List<GameAPI>
)

data class GameAPI(
   @Json(name = "id") val id: Int,
   @Json(name = "name") val name: String,
   @Json(name = "background_image") val background_image: String,
   @Json(name = "released" ) val released: String?,
   @Json(name = "metacritic") val metacritic: Int?
)

interface GamesAPIService {
    @GET("games")
    suspend fun searchGames(
        @Query("search") query: String,
        @Query("key") apiKey: String = GamesAPIConstants.TOKEN
    ) : GameSearchResponse
}