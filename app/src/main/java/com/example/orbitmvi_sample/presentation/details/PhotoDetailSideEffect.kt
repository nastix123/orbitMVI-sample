package com.example.orbitmvi_sample.presentation.details

sealed class PhotoDetailSideEffect {
    data object ShowProgressBar : PhotoDetailSideEffect()
    data object HideProgressBar : PhotoDetailSideEffect()
    data class NavigateToErrorScreen(val errorMessage: String) : PhotoDetailSideEffect()
}