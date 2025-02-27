package com.example.ratecontent.data.local.repository

import com.example.ratecontent.data.api.BookItem
import com.example.ratecontent.data.local.db.FavoriteBookDao
import com.example.ratecontent.data.local.entities.FavoriteBook
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(private val dao: FavoriteBookDao) {
    suspend fun insertFavorite(book: FavoriteBook) = dao.insert(book)
    fun getFavorites() : Flow<List<FavoriteBook>> = dao.getAllFavorites()
    suspend fun deleteFavorites(bookItemId: String) = dao.delete(bookItemId)
}