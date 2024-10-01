package com.example.orbitmvi_sample.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbitmvi_sample.domain.model.Image
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class PhotoDetailState(
    val image: Image? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PhotoDetailAction {
    data class LoadPhotoDetail(val photoId: Int) : PhotoDetailAction()
}

sealed class PhotoDetailSideEffect {
    data class ShowToast(val message: String) : PhotoDetailSideEffect()
}

class PhotoDetailViewModel(
) : ContainerHost<PhotoDetailState, PhotoDetailSideEffect>, ViewModel(), KoinComponent {

    override val container = container<PhotoDetailState, PhotoDetailSideEffect>(PhotoDetailState())
    private val repo: ApiRepository by inject()

    private val ceh = CoroutineExceptionHandler { _, exc ->
        intent {
            reduce {
                state.copy(isLoading = false, error = exc.message)
            }
        }
    }

    fun dispatch(action: PhotoDetailAction) {
        when (action) {
            is PhotoDetailAction.LoadPhotoDetail -> loadPhotoDetail(action.photoId)
        }
    }

    private fun loadPhotoDetail(photoId: Int) = intent {
        viewModelScope.launch (Dispatchers.IO + ceh) {
            reduce { state.copy(isLoading = true) }
            val image = repo.getPhoto(photoId)
            reduce { state.copy(image = image, isLoading = false, error = null) }
        }
    }
}

