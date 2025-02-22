package com.example.ratecontent.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ratecontent.data.api.ImageLinks

@Entity(tableName = "favorite_books")
data class FavoriteBook(
    @PrimaryKey val id: Int,
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val averageRating: Double?,
    val imageLinks: ImageLinks?
)