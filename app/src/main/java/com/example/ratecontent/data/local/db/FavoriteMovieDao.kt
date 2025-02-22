package com.example.ratecontent.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ratecontent.data.local.entities.FavoriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<FavoriteMovie>>

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun delete(movieId: Int)
}