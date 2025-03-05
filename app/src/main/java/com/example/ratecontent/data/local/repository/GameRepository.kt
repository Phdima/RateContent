package com.example.ratecontent.data.local.repository

import com.example.ratecontent.data.local.db.FavoriteGameDao
import com.example.ratecontent.data.local.db.FavoriteMovieDao
import com.example.ratecontent.data.local.entities.FavoriteGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameRepository @Inject constructor(private val dao: FavoriteGameDao){
    suspend fun insertFavorite(game: FavoriteGame) = dao.insert(game)
    fun getFavorites(): Flow<List<FavoriteGame>> = dao.getAllFavorites()
    suspend fun deleteFavorite(gameID: Int) = dao.delete(gameID)
}