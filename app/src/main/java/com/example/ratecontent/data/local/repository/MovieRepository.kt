package com.example.ratecontent.data.local.repository

import com.example.ratecontent.data.local.db.FavoriteMovieDao
import com.example.ratecontent.data.local.entities.FavoriteMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class MovieRepository @Inject constructor(private val dao: FavoriteMovieDao) {
    suspend fun insertFavorite(movie: FavoriteMovie) = dao.insert(movie)
    fun getFavorites(): Flow<List<FavoriteMovie>> = dao.getAllFavorites()
    suspend fun deleteFavorite(movieId: Int) = dao.delete(movieId)
}




