package com.example.ratecontent.domain

import android.util.Log
import com.example.ratecontent.data.api.Movie
import com.example.ratecontent.data.api.TMDbApiService
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val tmdbApiService: TMDbApiService
) {
    suspend fun searchMovies(query: String): List<Movie> {
        return try {
            val response = tmdbApiService.searchMovie(query = query)
            Log.d("SearchMoviesUseCase", "Searching movies for query: $query")
            if (response.results.isNotEmpty()) {
                response.results
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}


