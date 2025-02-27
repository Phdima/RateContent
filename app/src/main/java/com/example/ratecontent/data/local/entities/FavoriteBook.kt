package com.example.ratecontent.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ratecontent.data.api.ImageLinks

@Entity(tableName = "favorite_books")
data class FavoriteBook(
    @PrimaryKey val id: String,
    val title: String?,
    val description: String?,
    val averageRating: Double?,
    val imageLinks: ImageLinks?,
    val userRating: Double? = null
)