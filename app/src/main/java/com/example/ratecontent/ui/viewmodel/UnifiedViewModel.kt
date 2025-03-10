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
import com.example.ratecontent.data.api.GameAPI
import com.example.ratecontent.data.api.Movie
import com.example.ratecontent.data.local.entities.FavoriteBook
import com.example.ratecontent.data.local.entities.FavoriteGame
import com.example.ratecontent.data.local.entities.FavoriteMovie
import com.example.ratecontent.data.local.repository.BookRepository
import com.example.ratecontent.data.local.repository.MovieRepository
import com.example.ratecontent.data.local.entities.FavoriteItem
import com.example.ratecontent.data.local.repository.GameRepository
import com.example.ratecontent.domain.SearchGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnifiedViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchGamesUseCase: SearchGamesUseCase,
    private val movieRepository: MovieRepository,
    private val bookRepository: BookRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _movieResults = MutableLiveData<List<Movie>>(emptyList())
    private val _bookResults = MutableLiveData<List<BookItem>>(emptyList())
    private val _gamesResults = MutableLiveData<List<GameAPI>>(emptyList())
    val movieResults: LiveData<List<Movie>> get() = _movieResults
    val bookResults: LiveData<List<BookItem>> get() = _bookResults
    val gamesResults: LiveData<List<GameAPI>> get() = _gamesResults


    val favoriteMovies: LiveData<List<FavoriteMovie>> = movieRepository.getFavorites().asLiveData()
    val favoriteBooks: LiveData<List<FavoriteBook>> = bookRepository.getFavorites().asLiveData()
    val favoriteGames: LiveData<List<FavoriteGame>> = gameRepository.getFavorites().asLiveData()

    fun search(query: String) {
        viewModelScope.launch {
            val moviesDeferred = async {
                searchMoviesUseCase.searchMovies(query)
            }
            val booksDeferred = async {
                searchBooksUseCase.searchBooks(query)
            }
            val gamesDeferred = async {
                searchGamesUseCase.searchGames(query)
            }
            Log.d("GameSearch", "Search started: $query")
            val movies = moviesDeferred.await()
            val books = booksDeferred.await()
            val games = gamesDeferred.await()
            _movieResults.postValue(movies)
            _bookResults.postValue(books)
            _gamesResults.postValue(games)
            Log.d("GameSearch", "Results received: ")
        }
    }


    fun addToFavorites(item: FavoriteItem, userRating: Double) {
        viewModelScope.launch {
            when(item){
                is FavoriteItem.MovieFavorite -> {
                    val favoriteMovie = FavoriteMovie(
                        id = item.movieFavorite.id,
                        title = item.movieFavorite.title,
                        posterPath = item.movieFavorite.posterPath ?: "",
                        overview = item.movieFavorite.overview,
                        voteAverage = item.movieFavorite.voteAverage,
                        userRating = userRating
                    )
                    movieRepository.insertFavorite(favoriteMovie)
                }
                is FavoriteItem.BookFavorite -> {
                    val favoriteBook = FavoriteBook(
                        id = item.bookItem.id,
                        title = item.bookItem.title,
                        description = item.bookItem.description,
                        averageRating = item.bookItem.averageRating,
                        imageLinks = item.bookItem.imageLinks,
                        userRating = userRating
                    )
                    bookRepository.insertFavorite(favoriteBook)
                }
                is FavoriteItem.GameFavorite -> {
                    val favoriteGame = FavoriteGame(
                        id = item.gameFavorite.id,
                        name = item.gameFavorite.name,
                        background_image = item.gameFavorite.background_image,
                        released = item.gameFavorite.released,
                        metacritic = item.gameFavorite.metacritic,
                        userRating = userRating
                    )
                    gameRepository.insertFavorite(favoriteGame)
                }
            }


        }
    }

    fun removeFromFavorites(item: FavoriteItem) {
        when(item){
            is FavoriteItem.MovieFavorite -> {
                viewModelScope.launch {
                    movieRepository.deleteFavorite(item.movieFavorite.id)
                }
            }
            is FavoriteItem.BookFavorite -> {
                viewModelScope.launch {
                    bookRepository.deleteFavorites(item.bookItem.id)
                }
            }
            is FavoriteItem.GameFavorite -> {
                viewModelScope.launch {
                    gameRepository.deleteFavorite(item.gameFavorite.id)
                }
            }
        }
    }

}