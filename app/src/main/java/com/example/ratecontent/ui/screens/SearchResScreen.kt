package com.example.ratecontent.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ratecontent.data.api.GameAPI
import com.example.ratecontent.data.api.Movie
import com.example.ratecontent.data.local.entities.FavoriteBook
import com.example.ratecontent.data.local.entities.FavoriteItem
import com.example.ratecontent.data.local.entities.FavoriteMovie
import com.example.ratecontent.ui.viewmodel.UnifiedViewModel
import com.example.ratecontent.utils.MovieAPIConstants


sealed class UnifiedSearchResult {
    data class SectionHeader(val title: String) : UnifiedSearchResult()
    data class MovieItems(val movie: Movie) : UnifiedSearchResult()
    data class BookItems(val book: BookItem) : UnifiedSearchResult()
    data class GameItems(val game: GameAPI) : UnifiedSearchResult()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultScreen() {
    val navController = LocalNavController.current
    val parentEntry = remember { navController.getBackStackEntry("main_graph") }
    val viewModel: UnifiedViewModel = hiltViewModel(parentEntry)
    val movieSearchResults by viewModel.movieResults.observeAsState(emptyList())
    val bookSearchResults by viewModel.bookResults.observeAsState(emptyList())
    val gameSearchResult by viewModel.gamesResults.observeAsState(emptyList())

    val unifiedResults = remember(movieSearchResults, bookSearchResults) {
        buildList<UnifiedSearchResult> {
            if (movieSearchResults.isNotEmpty()) {
                add(UnifiedSearchResult.SectionHeader("Movies"))
                movieSearchResults.sortedByDescending { it.voteAverage }.forEach {
                    add(UnifiedSearchResult.MovieItems(it))
                }
            }
            if (bookSearchResults.isNotEmpty()) {
                add(UnifiedSearchResult.SectionHeader("Books"))
                bookSearchResults.sortedByDescending { it.volumeInfo.averageRating ?: 0.0 }
                    .forEach {
                        add(UnifiedSearchResult.BookItems(it))
                    }
            }
            if (gameSearchResult.isNotEmpty()) {
                add(UnifiedSearchResult.SectionHeader("Games"))
                gameSearchResult.sortedByDescending { it.metacritic }.forEach {
                    add(UnifiedSearchResult.GameItems(it))
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
                                onFavoriteClick = { userRating ->
                                    viewModel.addToFavorites(
                                        FavoriteItem.MovieFavorite(
                                            FavoriteMovie(
                                                id = result.movie.id,
                                                title = result.movie.title,
                                                posterPath = result.movie.posterPath ?: "",
                                                overview = result.movie.overview,
                                                voteAverage = result.movie.voteAverage
                                            )
                                        ), userRating = userRating
                                    )
                                }
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
                                onFavoriteClick = { userRating ->
                                    viewModel.addToFavorites(
                                        FavoriteItem.BookFavorite(
                                            FavoriteBook(
                                                id = result.book.id,
                                                title = result.book.volumeInfo.title,
                                                description = result.book.volumeInfo.description,
                                                averageRating = result.book.volumeInfo.averageRating,
                                                imageLinks = result.book.volumeInfo.imageLinks
                                            )
                                        ), userRating = userRating
                                    )
                                }
                            )
                        }
                    }

                    is UnifiedSearchResult.GameItems -> {
                        item {
                            SearchItemRow(
                                title = result.game.name,
                                overview = result.game.released.toString(),
                                imageUrl = result.game.background_image,
                                rating = (result.game.metacritic?.toDouble()?.div(10)) ?: 0.0,
                                onFavoriteClick = {}
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
    onFavoriteClick: (userRating: Double) -> Unit
) {

    var isFavorite by remember(title) { mutableStateOf(false) }
    var showRatingDialog by remember { mutableStateOf(false) }

    if (showRatingDialog) {
        RatingDialog(
            onRatingSelected = { selectedRating ->
                showRatingDialog = false
                isFavorite = true
                onFavoriteClick(selectedRating)
            },
            onDismiss = { showRatingDialog = false }
        )
    }

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
            if (rating != 0.0) {
                Text(
                    text = String.format("%.1f", rating),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                )
            } else Spacer(modifier = Modifier.height(24.dp))
            Icon(
                imageVector = if (!isFavorite) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                contentDescription = "Toggle Favorite",
                tint = Color.Red,
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
                    .clickable {
                        showRatingDialog = true
                    }
            )
        }
    }
}

@Composable
fun RatingDialog(
    initialRating: Float = 5f,
    onRatingSelected: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialRating) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Оцените по 10-бальной шкале") },
        text = {
            Column {
                Text("Оценка: ${sliderValue.toInt()}")
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..10f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onRatingSelected(sliderValue.toDouble()) }) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


