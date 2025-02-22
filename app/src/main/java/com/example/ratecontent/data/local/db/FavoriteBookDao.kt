package com.example.ratecontent.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ratecontent.data.api.BookItem
import com.example.ratecontent.data.local.entities.FavoriteBook
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: FavoriteBook)

    @Query("SELECT * FROM favorite_books")
    fun getAllFavorites(): Flow<List<FavoriteBook>>

    @Query("DELETE FROM favorite_books WHERE id = :bookItemId ")
    suspend fun delete(bookItemId: Int)
}