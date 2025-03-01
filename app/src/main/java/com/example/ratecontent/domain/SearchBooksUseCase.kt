package com.example.ratecontent.domain

import android.util.Log
import com.example.ratecontent.data.api.BookApiService
import com.example.ratecontent.data.api.BookItem
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookApiService: BookApiService
) {
    suspend fun searchBooks(query: String): List<BookItem> {

        return try {
            val response = bookApiService.searchBooks(query = query)
            response.items ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}