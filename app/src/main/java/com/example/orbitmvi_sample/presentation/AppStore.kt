package com.example.orbitmvi_sample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbitmvi_sample.domain.model.Image
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

sealed interface MainScreenAction {
    data object LoadPhotos : MainScreenAction
    data object Refresh : MainScreenAction
    data class SelectPhoto(val image: Image) : MainScreenAction
}

sealed class ImagesListSideEffect {
    data class ShowToast(val message: String) : ImagesListSideEffect()
    data class NavigateToPhotoDetail(val image: Image) : ImagesListSideEffect()
}

data class ImagesListState(
    val images: List<Image> = emptyList(),
    val selectedImage: Image? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean? = false
)


class AppViewModel() : ContainerHost<ImagesListState, ImagesListSideEffect>, ViewModel(), KoinComponent {
    private val scope: CoroutineScope by inject()
    private val repo: ApiRepository by inject()

    override val container =
        scope.container<ImagesListState, ImagesListSideEffect>(ImagesListState())

    init {
        dispatch(MainScreenAction.LoadPhotos)
    }

    private val ceh = CoroutineExceptionHandler { _, exc ->
        intent {
            reduce {
                state.copy(isLoading = false, error = exc.message)
            }
        }
    }

    fun dispatch(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.LoadPhotos -> loadPhotos()
            is MainScreenAction.Refresh -> loadPhotos()
            is MainScreenAction.SelectPhoto -> handleSelectPhoto(action.image)
        }
    }

    private fun loadPhotos() = intent {
        viewModelScope.launch(Dispatchers.IO + ceh) {
            reduce { state.copy(isLoading = true) }
            val images = fetchPhotos()
            reduce { state.copy(images = images, isLoading = false, error = null) }
        }
    }

    private fun handleSelectPhoto(image: Image) = intent {
        viewModelScope.launch(ceh) {
            reduce { state.copy(selectedImage = image) }
            postSideEffect(ImagesListSideEffect.NavigateToPhotoDetail(image))
        }
    }

    private suspend fun fetchPhotos(): List<Image> {
        val images = mutableListOf<Image>()
        repo.getPhotos().collect { photos ->
            images.addAll(photos)
        }
        return images
    }

}

