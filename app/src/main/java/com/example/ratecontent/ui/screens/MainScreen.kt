package com.example.ratecontent.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ratecontent.LocalNavController
import com.example.ratecontent.R
import com.example.ratecontent.ui.viewmodel.UnifiedViewModel
import com.example.ratecontent.utils.MovieAPIConstants


@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.main_color))
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp)
                )
                .background(color = colorResource(R.color.side_color))
                .fillMaxWidth()
                .height(100.dp)
        )

        SearchBar()
        LazyCardView()
    }
}


@Composable
fun SearchBar() {
    val navController = LocalNavController.current
    var searchText by remember { mutableStateOf("") }
    val parentEntry = remember { navController.getBackStackEntry("main_graph") }
    val viewModel: UnifiedViewModel = hiltViewModel(parentEntry)
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Поиск...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.search(searchText)
                    navController.navigate("SearchResultScreen")
                }
            ),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.search(searchText)
                    navController.navigate("SearchResultScreen")
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "кнопка поиска"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.main_color),
                focusedContainerColor = colorResource(R.color.main_color)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .shadow(5.dp, shape = RoundedCornerShape(50.dp))
                .clip(shape = RoundedCornerShape(50.dp))
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(50.dp))
        )
    }
}


@Composable
fun LazyCardView(viewModel: UnifiedViewModel = hiltViewModel()) {
    val cardList = listOf("Anime", "Movies", "Games", "Books")
    val favoriteMovies by viewModel.favoriteMovies.observeAsState(emptyList())
    var expandedItem by remember { mutableStateOf<String?>(null) }
    val sortedItems = if (expandedItem != null) {
        listOf(expandedItem!!) + cardList.filter { it != expandedItem }
    } else cardList

    val categoryMap = mapOf(
        "Movies" to favoriteMovies.sortedByDescending { it.voteAverage },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 110.dp)
    ) {
        sortedItems.forEach { item ->
            Column {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(10.dp))
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = colorResource(R.color.side_color))
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            if (expandedItem == null) {
                                expandedItem = item
                            } else expandedItem = null
                        }
                ) {
                    Text(
                        text = item,
                        color = Color.Black,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .padding(start = 30.dp)
                    )
                    val sortedMovies = categoryMap[item] ?: emptyList()
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .offset(x = 80.dp),
                    ) {
                        for (i in 0..4) {
                            val movie = sortedMovies.getOrNull(i)
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = MovieAPIConstants.IMAGE_BASE_URL + movie?.posterPath,
                                ),
                                contentDescription = "top 5 image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .offset(x = (i * 55).dp)
                                    .clip(shape = SlantedColumnShape())
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = SlantedColumnShape()
                                    )
                                    .background(color = colorResource(R.color.main_color))
                                    .fillMaxHeight()
                                    .width(110.dp)
                            )
                        }
                    }
                }
                if (expandedItem == item) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = colorResource(R.color.side_color))
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        when (item) {
                            "Movies" -> FavoritesMovieScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesMovieScreen(viewModel: UnifiedViewModel = hiltViewModel()) {
    val favoriteMovies by viewModel.favoriteMovies.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 125.dp)
        ) {

            items(favoriteMovies.sortedByDescending { it.voteAverage }) { movie ->
                var isFavorite by remember(movie.id) { mutableStateOf(true) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(8.dp)
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .background(colorResource(R.color.side_color))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(MovieAPIConstants.IMAGE_BASE_URL + movie.posterPath),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(100.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = movie.title,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp)
                        )
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .height(80.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(text = movie.overview)
                        }
                    }
                    Column {
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить из избранного",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(50.dp)
                                .offset(y = 50.dp)
                                .padding(8.dp)
                                .clickable {
                                    viewModel.removeFromFavorites(movie)
                                }
                        )
                    }
                }
            }
        }
    }
}



class SlantedColumnShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ) = androidx.compose.ui.graphics.Outline.Generic(
        Path().apply {
            moveTo(size.width, 0f) // правый верхний
            lineTo(size.width - (size.width / 2), size.height) // правый нижний
            lineTo(0f, size.height) // левый нижний
            lineTo(size.width / 2, 0f) // левый верхний
            close()
        }
    )
}