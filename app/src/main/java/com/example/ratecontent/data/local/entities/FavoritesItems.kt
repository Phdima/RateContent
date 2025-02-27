package com.example.ratecontent.data.local.entities

import com.example.ratecontent.data.api.BookItem
import com.example.ratecontent.data.api.Movie


sealed class FavoriteItem {
    data class MovieFavorite(val movieFavorite: FavoriteMovie) : FavoriteItem()
    data class BookFavorite(val bookItem: FavoriteBook) : FavoriteItem()
}
