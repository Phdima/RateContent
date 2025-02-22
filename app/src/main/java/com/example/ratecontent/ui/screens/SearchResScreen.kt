package com.example.ratecontent.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ratecontent.LocalNavController
import com.example.ratecontent.R
import com.example.ratecontent.data.api.BookItem
import com.example.ratecontent.data.api.Movie
import com.example.ratecontent.ui.viewmodel.UnifiedViewModel
import com.example.ratecontent.utils.MovieAPIConstants


sealed class UnifiedSearchResult {
    data class SectionHeader(val title: String) : UnifiedSearchResult()
    data class MovieItems(val movie: Movie) : UnifiedSearchResult()
    data class BookItems(val book: BookItem) : UnifiedSearchResult()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultScreen() {
    val navController = LocalNavController.current
    val parentEntry = remember { navController.getBackStackEntry("main_graph") }
    val viewModel: UnifiedViewModel = hiltViewModel(parentEntry)
    val movieSearchResults by viewModel.movieResults.observeAsState(emptyList())
    val bookSearchResults by viewModel.bookResults.observeAsState(emptyList())

    val unifiedResults = remember(movieSearchResults, bookSearchResults) {
        buildList<UnifiedSearchResult> {
            if (movieSearchResults.isNotEmpty()) {
                add(UnifiedSearchResult.SectionHeader("Movies"))
                movieSearchResults.sortedByDescending { it.voteAverage }.forEach {
                    add(UnifiedSearchResult.MovieItems(it))
                }
            }
            if (bookSearchResults.isNotEmpty()){
                add(UnifiedSearchResult.SectionHeader("Books"))
                bookSearchResults.sortedByDescending { it.volumeInfo.averageRating ?: 0.0 }.forEach{
                    add(UnifiedSearchResult.BookItems(it))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.main_color))
    ) {
        LazyColumn {
            unifiedResults.forEach { result ->
                when (result) {
                    is UnifiedSearchResult.SectionHeader -> {
                        stickyHeader {
                            Text(
                                text = result.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Gray)
                                    .padding(8.dp)
                            )
                        }
                    }
                    is UnifiedSearchResult.MovieItems -> {
                        item {
                            SearchItemRow(
                                title = result.movie.title,
                                overview = result.movie.overview,
                                imageUrl = MovieAPIConstants.IMAGE_BASE_URL + result.movie.posterPath,
                                rating = result.movie.voteAverage,
                                //isFavorite = ,
                                onFavoriteClick = { viewModel.addToFavorites(result.movie) }
                            )
                        }
                    }

                    is UnifiedSearchResult.BookItems -> {
                        item {
                            SearchItemRow(
                                title = result.book.volumeInfo.title,
                                overview = result.book.volumeInfo.description ?: "Нет описания",
                                imageUrl = result.book.volumeInfo.imageLinks?.thumbnail,
                                rating = result.book.volumeInfo.averageRating ?: 0.0,
                                //isFavorite = /* Если нужна логика для избранного книг */,
                                onFavoriteClick = { /* Добавьте соответствующий обработчик */ }
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SearchItemRow(
    title: String,
    overview: String,
    imageUrl: String?,
    rating: Double,
    //isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
            .background(colorResource(id = R.color.side_color))
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl ?: ""),
            contentDescription = "$title Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(140.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(5.dp)
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = overview)
            }
        }
        Column {
            Text(
                text = String.format("%.1f", rating),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
            )
//            Icon(
//                imageVector = if (!isFavorite) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
//                contentDescription = "Toggle Favorite",
//                tint = Color.Red,
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(5.dp)
//                    .clickable { onFavoriteClick() }
//            )
        }
    }
}


