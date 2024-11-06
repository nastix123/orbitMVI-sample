package com.example.orbitmvi_sample.presentation.list

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.orbitmvi_sample.domain.model.Image
import com.example.orbitmvi_sample.presentation.AppViewModel
import com.example.orbitmvi_sample.presentation.ImagesListSideEffect
import com.example.orbitmvi_sample.presentation.MainScreenAction
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.random.Random

@Composable
fun ImagesScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Text(text = "${state.error}", modifier = Modifier.align(Alignment.Center))
            }

            state.images.isNotEmpty() -> {
                ImagesList(
                    images = state.images,
                    onItemClick = { photo ->
                        viewModel.dispatch(MainScreenAction.SelectPhoto(photo))
                    },
                    onDeleteClick = { photo ->
                        viewModel.dispatch(MainScreenAction.DeletePhoto(photo))
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

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ImagesListSideEffect.ShowToast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ImagesListSideEffect.NavigateToPhotoDetail -> {
                navController.navigate("photo_detail_screen_route/${sideEffect.image.id}")
            }

        }
    }
}


@Composable
fun ImagesList(
    images: List<Image>,
    onItemClick: (Image) -> Unit,
    onDeleteClick: (Image) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(
            count = images.size,
            key = { it ->
                images[it].id
            }
        ) { image ->
            ImageItem(
                image = images[image],
                onClick = onItemClick,
                onDeleteClick = onDeleteClick,
            )
        }
    }
}

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    image: Image,
    onClick: (Image) -> Unit,
    onDeleteClick: (Image) -> Unit // Добавляем обработчик удаления
) {
    Card(
        modifier = modifier
            .clickable { onClick.invoke(image) }
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        val context = LocalContext.current
        val listener = object : ImageRequest.Listener {
            override fun onError(request: ImageRequest, result: ErrorResult) {
                super.onError(request, result)
            }

            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                super.onSuccess(request, result)
            }
        }
        val imageRequest = ImageRequest.Builder(context)
            .data(image.url)
            .listener(listener)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(image.url)
            .diskCacheKey(image.url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()

        Column {
            AsyncImage(
                model = imageRequest,
                contentDescription = image.photographer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Text(
                    text = image.photographer,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterStart),
                    fontSize = 16.sp,
                    color = Color.White
                )
                IconButton(
                    onClick = { onDeleteClick(image) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color.White
                    )
                }
            }
        }
    }
}




