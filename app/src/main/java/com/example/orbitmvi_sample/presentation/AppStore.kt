package com.example.orbitmvi_sample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbitmvi_sample.domain.model.Image
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container

sealed class MainScreenAction {
    data object LoadPhotos : MainScreenAction()
    data class DeletePhoto(val image: Image) : MainScreenAction()
    data class SelectPhoto(val image: Image) : MainScreenAction()
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
            is MainScreenAction.DeletePhoto -> deletePhoto(action.image)
            is MainScreenAction.SelectPhoto -> handleSelectPhoto(action.image)
        }
    }


    private fun loadPhotos() = intent {
        viewModelScope.launch(ceh) {
            reduce { state.copy(isLoading = true) }

            repo.getPhotos().collect { images ->
                reduce { state.copy(images = images, isLoading = false, error = null) }
            }
        }
    }

    private fun deletePhoto(image: Image) = intent {
        viewModelScope.launch(ceh) {
            val updatedImages = state.images.filter { it.id != image.id }
            reduce { state.copy(images = updatedImages) }
            postSideEffect(ImagesListSideEffect.ShowToast("Фото удалено"))
        }
    }

    private fun handleSelectPhoto(image: Image) = intent {
        viewModelScope.launch(ceh) {
            reduce { state.copy(selectedImage = image) }
            postSideEffect(ImagesListSideEffect.NavigateToPhotoDetail(image))
        }
    }

    fun loadPhotosRepeatOnSub() = intent(registerIdling = false) {
        //гарантирует, что собираем изображения только тогда, когда подписка на UI активна
        repeatOnSubscription {
            repo.getPhotos().collect { images ->
                reduce { state.copy(images = images, isLoading = false, error = null) }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    suspend fun filterImages() = subIntent {
        val filteredImages = state.images.filter { it.photographer.contains("John") }
        reduce { state.copy(images = filteredImages) }
    }

    @OptIn(OrbitExperimental::class)
    suspend fun fetchImages() = subIntent {
        repo.getPhotos().collect { images ->
            reduce { state.copy(images = images) }
        }
    }

    fun loadAndFilterPhotos() = intent {
        fetchImages()
        filterImages()
    }
}

