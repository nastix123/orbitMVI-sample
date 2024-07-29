package com.example.orbitmvi_sample.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.orbitmvi_sample.domain.model.Photo
import com.example.orbitmvi_sample.presentation.AppViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ImagesScreen(
    viewModel: AppViewModel
) {
    val state by viewModel.container.stateFlow.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.error != null -> {
                Text(
                    text = state.error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            state.images.isNotEmpty() -> {
                ImagesList(
                    images = state.images,
                    onItemClick = { photo ->
                        // TODO: Обработка клика на изображение
                    }
                )
            }
            else -> {
                Text(
                    text = "Нет доступных изображений",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
    viewModel.collectSideEffect {
        when (it) {
            is ImagesListSideEffect.ShowToast -> {
                // TODO: show toast
            }
            is ImagesListSideEffect.ShowProgressBar -> {
                // TODO: show progress bar
            }
            is ImagesListSideEffect.HideProgressBar -> {
                // TODO: hide progress bar
            }
            is ImagesListSideEffect.NavigateToErrorScreen -> {
                // TODO: navigate to error screen
            }
        }
    }
}

@Composable
fun ImagesList(
    images: List<Photo>,
    onItemClick: (Photo) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(images) { image ->
            ImageItem(
                photoData = image,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClick(image) }
            )
        }
    }
}

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    photoData: Photo,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),

    ) {
        Column {

            AsyncImage(
                model = photoData.photoUrl,
                contentDescription = photoData.photographer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = photoData.photographer ?: "Без описания",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontSize = 22.sp
            )
        }
    }
}

