package com.example.ratecontent

import android.widget.ImageButton
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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun SearchResultScreen() {
    val navController = LocalNavController.current
    val parentEntry = remember { navController.getBackStackEntry("main_graph") }
    val viewModel: MovieViewModel = hiltViewModel(parentEntry)
    val searchResults by viewModel.searchResult.observeAsState(emptyList())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.main_color))
    ) {
        LazyColumn {
            items(searchResults.sortedByDescending { movie -> movie.voteAverage }) { movie ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(8.dp)
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .background(colorResource(R.color.side_color))
                )
                {
                    Image(
                        painter = rememberAsyncImagePainter(MovieAPIConstants.IMAGE_BASE_URL + movie.posterPath),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(100.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "${movie.title} ",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp)

                        )
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .height(80.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = movie.overview
                            )
                        }
                    }
                    Column()
                    {
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        )
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "добавление в избранное",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                                .clickable {  }
                        )
                    }
                }
            }
        }
    }
}
