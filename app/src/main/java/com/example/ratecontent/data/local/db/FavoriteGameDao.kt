package com.example.ratecontent.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ratecontent.data.local.entities.FavoriteGame
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: FavoriteGame)

    @Query("SELECT * FROM favorite_games")
    fun getAllFavorites(): Flow<List<FavoriteGame>>

    @Query("DELETE FROM favorite_games WHERE id = :gameID")
    suspend fun delete(gameID: Int)
}
