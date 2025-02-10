package com.example.ratecontent

import android.app.appsearch.SearchResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState


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
        LazyCardView(items = cardList)
    }
}


@Composable
fun SearchBar() {
    val navController = LocalNavController.current
    var searchText by remember { mutableStateOf("") }
    val parentEntry = remember { navController.getBackStackEntry("main_graph") }
    val viewModel: MovieViewModel = hiltViewModel(parentEntry)
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Поиск...") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.searchForMovie(searchText)
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
                .clip(shape = RoundedCornerShape(50.dp))
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(50.dp))
        )
    }
}


@Composable
fun LazyCardView(items: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 110.dp)
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillParentMaxWidth()
                    .height(100.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(color = colorResource(R.color.side_color))
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = item,
                    color = Color.Black,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterStart)
                        .padding(start = 30.dp)
                )
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterStart)
                        .offset(x = 80.dp),
                ) {
                    for (i in 0..4) Box(
                        modifier = Modifier
                            .offset(x = (i * 55).dp)
                            .clip(shape = SlantedColumnShape())
                            .border(width = 1.dp, color = Color.Gray, shape = SlantedColumnShape())
                            .background(color = Color.Cyan)
                            .fillMaxHeight()
                            .width(110.dp)
                    ) {
                        Text(text = "Test")
                    }
                }
            }
        }
    }
}

val cardList = listOf("Anime", "Movies", "Games", "Books")

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