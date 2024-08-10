package com.example.orbitmvi_sample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import com.example.orbitmvi_sample.presentation.list.ImagesListSideEffect
import com.example.orbitmvi_sample.presentation.list.ImagesListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AppViewModel(
    private val scope: CoroutineScope,
    private val repo: ApiRepository
) : ContainerHost<ImagesListState, ImagesListSideEffect>, ViewModel(), KoinComponent {
    override val container =
        scope.container<ImagesListState, ImagesListSideEffect>(ImagesListState())

    init {
        viewModelScope.launch {
            getPhotos()
        }

    }
    private fun getPhotos() = intent {
        Log.d("AppViewModel", "getPhotos() called")
        try {
            postSideEffect(ImagesListSideEffect.ShowProgressBar)
            val flowOfImages = repo.getPhotos()
            Log.d("AppViewModel", "repo.getPhotos() returned")
            flowOfImages.collect { images ->
                Log.d("AppViewModel", "Images collected: ${images.size} items")
                reduce { state.copy(images = images) }
            }
            Log.d("AppViewModel", "new images were added")
        } catch (e: Exception) {
            Log.e("AppViewModel", "Exception: ${e.message}")
            postSideEffect(
                ImagesListSideEffect.NavigateToErrorScreen(
                    e.message ?: "Неизвестная ошибка"
                )
            )
        } finally {
            postSideEffect(ImagesListSideEffect.HideProgressBar)
        }
    }

    fun getPhotoById(photoId: Int) = intent {
        postSideEffect(ImagesListSideEffect.ShowProgressBar)
        try {
            val photo = repo.getPhoto(photoId)
            reduce { state.copy(selectedPhoto = photo, isLoading = false) }
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false, error = e.message) }
            postSideEffect(
                ImagesListSideEffect.NavigateToErrorScreen(
                    e.message ?: "Неизвестная ошибка"
                )
            )
        } finally {
            postSideEffect(ImagesListSideEffect.HideProgressBar)
        }
    }
}