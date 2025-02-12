package com.example.ratecontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val apiService: TMDbApiService,
    private val repository: MovieRepository
) : ViewModel() {
    private val _searchResult = MutableLiveData<List<Movie>>(emptyList())
    val searchResult: LiveData<List<Movie>> get() = _searchResult

    fun searchForMovie(userQuery: String) {
        apiService.searchMovie(query = userQuery)
            .enqueue(object : retrofit2.Callback<SearchMovieResponse> {

                override fun onResponse(
                    call: retrofit2.Call<SearchMovieResponse>,
                    response: retrofit2.Response<SearchMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        _searchResult.postValue(response.body()?.results ?: emptyList())
                    } else {
                        _searchResult.postValue(emptyList())
                    }
                }

                override fun onFailure(call: retrofit2.Call<SearchMovieResponse>, t: Throwable) {
                    println("Ошибка сети: ${t.message}")
                }
            })
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

}