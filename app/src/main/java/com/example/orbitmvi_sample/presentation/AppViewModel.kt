package com.example.orbitmvi_sample.presentation

import androidx.lifecycle.ViewModel
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import com.example.orbitmvi_sample.presentation.list.ImagesListSideEffect
import com.example.orbitmvi_sample.presentation.list.ImagesListState
import kotlinx.coroutines.CoroutineScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AppViewModel(
    private val scope: CoroutineScope,
    private val repo: ApiRepository
) : ContainerHost<ImagesListState, ImagesListSideEffect>, ViewModel() {
    override val container =
        scope.container<ImagesListState, ImagesListSideEffect>(ImagesListState())

    init {
        getPhotos()
    }
    private fun getPhotos() = intent {
        repeatOnSubscription {
            try {
                postSideEffect(ImagesListSideEffect.ShowProgressBar)
                val flowOfImages = repo.getPhotos()
                flowOfImages.collect { images ->
                    reduce { state.copy(images = images) }
                }
            } catch (e: Exception) {
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