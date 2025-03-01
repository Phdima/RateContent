package com.example.ratecontent.domain

import android.util.Log
import com.example.ratecontent.data.api.GameAPI
import com.example.ratecontent.data.api.GamesAPIService
import retrofit2.http.Query
import javax.inject.Inject

class SearchGamesUseCase @Inject constructor(
    private val gamesAPIService: GamesAPIService
){
    suspend fun searchGames(query: String): List<GameAPI> {
        return try {
            val response = gamesAPIService.searchGames(query = query)
            response.results ?: emptyList()
        }catch (e: Exception){
            emptyList()
        }
    }
}