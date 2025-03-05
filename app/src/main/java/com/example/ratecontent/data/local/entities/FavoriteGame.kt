package com.example.ratecontent.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_games")
data class FavoriteGame (
    @PrimaryKey val id: Int,
    val name: String,
    val background_image: String,
    val released: String?,
    val metacritic: Int?,
    val userRating: Double? = null
)
