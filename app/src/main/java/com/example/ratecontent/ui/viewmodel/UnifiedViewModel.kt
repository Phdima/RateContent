package com.example.ratecontent.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ratecontent.domain.SearchBooksUseCase
import com.example.ratecontent.domain.SearchMoviesUseCase
import com.example.ratecontent.data.api.BookItem
import com.example.ratecontent.data.api.Movie
import com.example.ratecontent.data.local.entities.FavoriteMovie
import com.example.ratecontent.data.local.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnifiedViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieResults = MutableLiveData<List<Movie>>(emptyList())
    private val _bookResults = MutableLiveData<List<BookItem>>(emptyList())


    val movieResults: LiveData<List<Movie>> get() = _movieResults
    val bookResults: LiveData<List<BookItem>> get() = _bookResults


    val favoriteMovies: LiveData<List<FavoriteMovie>> = repository.getFavorites().asLiveData()


    fun search(query: String) {

        viewModelScope.launch {
            val moviesDeferred = async {
                searchMoviesUseCase.searchMovies(query)
            }

          val booksDeferred = async {

               searchBooksUseCase.searchBooks(query)
           }

            val movies = moviesDeferred.await()
            val books = booksDeferred.await()

            _movieResults.postValue(movies)

            _bookResults.postValue(books)

        }
    }


    fun addToFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorite = FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                overview = movie.overview,
                voteAverage = movie.voteAverage
            )
           repository.insertFavorite(favorite)
        }
    }

    fun removeFromFavorites(movie: FavoriteMovie){
        viewModelScope.launch {
            repository.deleteFavorite(movie.id)
        }
    }

}